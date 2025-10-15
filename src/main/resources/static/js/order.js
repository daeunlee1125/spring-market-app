document.addEventListener('DOMContentLoaded', function () {

    // =======================================================
    // 🚀 1. 배송지 변경 로직 (기존과 동일)
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
    // 🚀 2. 최종 결제 정보 동적 계산 로직 (기존과 동일)
    // =======================================================
    const pointsInput = document.getElementById('pointsToUseInput');
    const usePointsBtn = document.getElementById('usePointsBtn');
    const couponSelect = document.getElementById('couponSelect');

    const summaryOrderAmountElem = document.getElementById('summaryOrderAmount');
    const summaryShippingFeeElem = document.getElementById('summaryShippingFee');
    const summaryTotalDiscountElem = document.getElementById('summaryTotalDiscount');
    const summaryProductDiscountElem = document.getElementById('summaryProductDiscount');
    const summaryPointsDiscountElem = document.getElementById('summaryPointsDiscount');
    const summaryCouponDiscountElem = document.getElementById('summaryCouponDiscount');
    const summaryFinalPaymentElem = document.getElementById('summaryFinalPayment');

    const orderAmount = summaryOrderAmountElem ? parseInt(summaryOrderAmountElem.dataset.price, 10) : 0;
    const shippingFee = summaryShippingFeeElem ? parseInt(summaryShippingFeeElem.dataset.price, 10) : 0;
    const productDiscountElem = document.getElementById('productDiscount');
    const productDiscount = productDiscountElem ? parseInt(productDiscountElem.dataset.price, 10) : 0;
    const totalPointsElem = document.querySelector('[data-total-points]');
    const totalPointsAvailable = totalPointsElem ? parseInt(totalPointsElem.dataset.totalPoints, 10) : 0;

    function updateFinalSummary() {
        let pointsToUse = parseInt(pointsInput.value, 10);
        if (isNaN(pointsToUse) || pointsToUse < 0) pointsToUse = 0;

        let couponDiscount = 0;
        let currentShippingFee = shippingFee;

        if (couponSelect && couponSelect.options.length > 0) {
            const selectedOption = couponSelect.options[couponSelect.selectedIndex];
            const cpType = parseInt(selectedOption.dataset.cpType, 10);
            const cpValue = parseInt(selectedOption.dataset.cpValue, 10);
            const cpMinPrice = parseInt(selectedOption.dataset.cpMinPrice, 10);

            if (orderAmount >= cpMinPrice) {
                switch (cpType) {
                    case 1:
                        couponDiscount = cpValue;
                        break;
                    case 2:
                        couponDiscount = Math.floor(orderAmount * (cpValue / 100));
                        break;
                    case 3:
                        currentShippingFee = 0;
                        couponDiscount = shippingFee; // 실제 할인액은 배송비만큼
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
            if (pointsToUse > (orderAmount + shippingFee - productDiscount)) {
                alert('포인트는 할인 후 결제 금액을 초과하여 사용할 수 없습니다.');
                pointsInput.value = orderAmount + shippingFee - productDiscount;
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

    updateFinalSummary();

    // =======================================================
    // 🚀 3. 최종 결제 버튼 로직 (수정된 부분)
    // =======================================================
    const mainOrderBtn = document.getElementById('mainOrderBtn');

    if (mainOrderBtn) {
        mainOrderBtn.addEventListener('click', function(event) {
            // form의 기본 제출 동작을 막습니다.
            event.preventDefault();

            if (confirm('결제를 진행하시겠습니까?')) {
                // 1. 서버로 보낼 데이터 수집 및 객체 생성
                // 사용한 포인트
                const usedPoints = parseInt(document.getElementById('pointsToUseInput').value, 10) || 0;

                // 사용한 쿠폰 ID
                const couponSelect = document.getElementById('couponSelect');
                const usedCouponId = couponSelect.value;

                // 받는 분 정보 (memberDTO)
                const recipientNameHp = document.querySelector('.info-table p strong').textContent.trim();
                const memberDTO = {
                    mem_name: recipientNameHp.split(' ')[0],
                    mem_hp: recipientNameHp.split(' ')[1],
                    mem_zip: document.getElementById('mem_zip').value,
                    mem_addr1: document.getElementById('mem_addr1').value,
                    // HTML 수정 제안에 따라 id="mem_addr2_input" 사용
                    mem_addr2: document.getElementById('mem_addr2_input').value
                };

                // 주문 상품 목록 (cartDTOList)
                // HTML 수정 제안에 따라 data-cart-id 사용
                const cartItems = [];
                document.querySelectorAll('.order-item').forEach(item => {
                    const cartId = item.dataset.cartId;
                    if(cartId) {
                        cartItems.push({
                            cart_id: parseInt(cartId, 10)
                            // 서버에서는 cart_id를 받아 DB에서 다시 상품 정보를 조회하는 것이 보안상 안전합니다.
                        });
                    }
                });

                // 최종 결제 금액 정보
                const finalPaymentText = document.getElementById('summaryFinalPayment').textContent;
                const finalPaymentAmount = parseInt(finalPaymentText.replace(/[^0-9]/g, ''), 10);

                // 선택된 결제 방법 텍스트 가져오기
                const checkedPaymentInput = document.querySelector('input[name="payment"]:checked');
                let paymentMethod = ''; // 기본값 설정
                if (checkedPaymentInput) {
                    paymentMethod = checkedPaymentInput.parentElement.querySelector('strong').textContent.trim();
                }


                // 2. 모든 데이터를 하나의 payload 객체로 합치기
                const payload = {
                    usedPoints: usedPoints,
                    usedCouponId: usedCouponId,
                    memberDTO: memberDTO,
                    orderItems: cartItems,
                    paymentMethod: paymentMethod,
                    finalAmount: finalPaymentAmount // 최종 결제 금액

                    // 필요하다면 기존 form의 hidden input 값들도 추가할 수 있습니다.
                    // totalprice: document.querySelector('input[name="totalprice"]').value
                };

                // 3. fetch API를 사용하여 서버에 POST 요청 보내기
                fetch('/shoply/api/product/order', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(payload) // JavaScript 객체를 JSON 문자열로 변환
                })
                    .then(response => {
                        if (!response.ok) {
                            // 서버에서 에러 응답이 온 경우
                            throw new Error('결제 처리 중 서버 오류가 발생했습니다.');
                        }
                        return response.json(); // 서버의 응답을 JSON으로 파싱
                    })
                    .then(data => {
                        // 4. 서버로부터 성공 응답을 받은 후, 받은 데이터로 주문 완료 페이지 URL을 만들어 이동합니다.
                        console.log('주문 성공:', data);

                        // URL 쿼리 파라미터를 만들기 위해 URLSearchParams 객체 사용
                        const params = new URLSearchParams();
                        params.append('orderId', data.orderId);
                        data.cartNoList.forEach(cartNo => {
                            params.append('cartNoList', cartNo);
                        });
                        if (data.cpCode) {
                            params.append('cpCode', data.cpCode);
                        }
                        params.append('usedPoint', data.usedPoint);

                        // 최종적으로 생성된 URL로 페이지를 이동시킵니다.
                        window.location.href = `/shoply/product/complete?${params.toString()}`;
                    })
                    .catch(error => {
                        console.error('결제 실패:', error);
                        alert(error.message);
                    });
            }
        });
    }
});