document.addEventListener('DOMContentLoaded', function () {

    // =======================================================
    // 🚀 1. 배송지 변경 로직
    // =======================================================
    const changeAddressBtn = document.getElementById('change-address-btn');

    if (changeAddressBtn) {
        const addressDisplay = document.getElementById('address-display');
        const zipInput = document.getElementById('mem_zip');
        const addr1Input = document.getElementById('mem_addr1');

        changeAddressBtn.addEventListener('click', function () {
            new daum.Postcode({
                oncomplete: function(data) {
                    let fullAddr = '';
                    let extraAddr = '';

                    if (data.userSelectedType === 'R') {
                        fullAddr = data.roadAddress;
                    } else {
                        fullAddr = data.jibunAddress;
                    }

                    if (data.userSelectedType === 'R') {
                        if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                            extraAddr += data.bname;
                        }
                        if (data.buildingName !== '' && data.apartment === 'Y') {
                            extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                        }
                        fullAddr += (extraAddr !== '' ? ' (' + extraAddr + ')' : '');
                    }

                    zipInput.value = data.zonecode;
                    addr1Input.value = fullAddr;
                    addressDisplay.innerText = `[${data.zonecode}] ${fullAddr}`;
                }
            }).open();
        });
    }

    // =======================================================
    // 🚀 2. 최종 결제 정보 동적 계산 로직
    // =======================================================
    const pointsInput = document.getElementById('pointsToUseInput');
    const usePointsBtn = document.getElementById('usePointsBtn');
    const couponSelect = document.getElementById('couponSelect'); // couponSelect 변수 선언

    const summaryOrderAmountElem = document.getElementById('summaryOrderAmount');
    const summaryShippingFeeElem = document.getElementById('summaryShippingFee');
    const summaryTotalDiscountElem = document.getElementById('summaryTotalDiscount');
    const summaryProductDiscountElem = document.getElementById('summaryProductDiscount');
    const summaryPointsDiscountElem = document.getElementById('summaryPointsDiscount');
    const summaryCouponDiscountElem = document.getElementById('summaryCouponDiscount');
    const summaryFinalPaymentElem = document.getElementById('summaryFinalPayment');

    const orderAmount = summaryOrderAmountElem ? parseInt(summaryOrderAmountElem.dataset.price, 10) : 0;
    const shippingFee = summaryShippingFeeElem ? parseInt(summaryShippingFeeElem.dataset.price, 10) : 0; // 초기 배송비
    const productDiscountElem = document.getElementById('productDiscount');
    const productDiscount = productDiscountElem ? parseInt(productDiscountElem.dataset.price, 10) : 0;
    const totalPointsElem = document.querySelector('[data-total-points]');
    const totalPointsAvailable = totalPointsElem ? parseInt(totalPointsElem.dataset.totalPoints, 10) : 0;

    /**
     * 최종 결제 정보를 다시 계산하고 화면을 업데이트하는 메인 함수
     */
    function updateFinalSummary() {
        let pointsToUse = parseInt(pointsInput.value, 10);
        if (isNaN(pointsToUse) || pointsToUse < 0) pointsToUse = 0;

        let couponDiscount = 0;
        let currentShippingFee = shippingFee; // 오타 수정: initialShippingFee -> shippingFee

        if (couponSelect && couponSelect.options.length > 0) {
            const selectedOption = couponSelect.options[couponSelect.selectedIndex];
            const cpType = parseInt(selectedOption.dataset.cpType, 10);
            const cpValue = parseInt(selectedOption.dataset.cpValue, 10);
            const cpMinPrice = parseInt(selectedOption.dataset.cpMinPrice, 10);

            if (orderAmount >= cpMinPrice) {
                switch (cpType) {
                    case 1: // 정액 할인
                        couponDiscount = cpValue;
                        break;
                    case 2: // 정률 할인
                        couponDiscount = Math.floor(orderAmount * (cpValue / 100));
                        break;
                    case 3: // 배송비 무료
                        currentShippingFee = 0;
                        couponDiscount = 0;
                        break;
                }
            }
        }

        const totalDiscount = productDiscount + pointsToUse + couponDiscount;
        const finalPayment = orderAmount + currentShippingFee - totalDiscount;

        const formatAsCurrency = (amount) => amount.toLocaleString('ko-KR');

        if(summaryShippingFeeElem) summaryShippingFeeElem.textContent = `+ ${formatAsCurrency(currentShippingFee)}원`;
        if(summaryProductDiscountElem) summaryProductDiscountElem.textContent = `- ${formatAsCurrency(productDiscount)}원`;
        if(summaryPointsDiscountElem) summaryPointsDiscountElem.textContent = `- ${formatAsCurrency(pointsToUse)}원`;
        if(summaryCouponDiscountElem) summaryCouponDiscountElem.textContent = `- ${formatAsCurrency(couponDiscount)}원`;
        if(summaryTotalDiscountElem) summaryTotalDiscountElem.textContent = `- ${formatAsCurrency(totalDiscount)}원`;
        if(summaryFinalPaymentElem) summaryFinalPaymentElem.textContent = `${formatAsCurrency(finalPayment)}원`;
    }

    if (usePointsBtn) {
        usePointsBtn.addEventListener('click', function() {
            let pointsToUse = parseInt(pointsInput.value, 10);

            if (isNaN(pointsToUse)) {
                alert('사용할 포인트는 숫자로 입력해주세요.');
                pointsInput.value = 0;
                updateFinalSummary();
                return;
            }
            if (pointsToUse > totalPointsAvailable) {
                alert(`보유 포인트를 초과하여 사용할 수 없습니다. (최대: ${totalPointsAvailable.toLocaleString('ko-KR')}P)`);
                pointsInput.value = totalPointsAvailable;
                updateFinalSummary();
                return;
            }
            if (pointsToUse > orderAmount - productDiscount) {
                alert('포인트는 상품 할인 후 금액을 초과하여 사용할 수 없습니다.');
                pointsInput.value = orderAmount - productDiscount;
                updateFinalSummary();
                return;
            }

            alert(`${pointsToUse.toLocaleString('ko-KR')}P가 적용되었습니다.`);
            updateFinalSummary();
        });
    }

    if (couponSelect) {
        couponSelect.addEventListener('change', function() {
            const selectedOption = this.options[this.selectedIndex];
            const cpMinPrice = parseInt(selectedOption.dataset.cpMinPrice, 10);

            if (orderAmount < cpMinPrice) {
                alert(`해당 쿠폰은 ${cpMinPrice.toLocaleString('ko-KR')}원 이상 구매 시 사용 가능합니다.`);
                this.selectedIndex = 0;
            }
            updateFinalSummary();
        });
    }

    // 페이지가 처음 로드될 때 한 번 실행하여 초기 값을 설정
    updateFinalSummary();

    // =======================================================
    // 🚀 3. 최종 결제 버튼 로직
    // =======================================================
    const orderForm = document.getElementById('orderForm');

    const handleOrderClick = function(event) {
        event.preventDefault();

        if (confirm('결제를 진행하시겠습니까?')) {
            if(orderForm) {
                orderForm.submit();
            } else {
                alert('결제 폼을 찾을 수 없습니다.');
            }
        }
    };

    const mainOrderBtn = document.getElementById('mainOrderBtn');
    if (mainOrderBtn) {
        mainOrderBtn.addEventListener('click', handleOrderClick);
    }

    const sideOrderBtn = document.getElementById('sideOrderBtn');
    if (sideOrderBtn) {
        sideOrderBtn.addEventListener('click', handleOrderClick);
    }
});