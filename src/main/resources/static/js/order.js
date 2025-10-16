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
                    case 1: couponDiscount = cpValue; break;
                    case 2: couponDiscount = Math.floor(orderAmount * (cpValue / 100)); break;
                    case 3:
                        currentShippingFee = 0;
                        couponDiscount = shippingFee;
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
            } else if (pointsToUse > totalPointsAvailable) {
                alert(`보유 포인트를 초과하여 사용할 수 없습니다. (최대: ${totalPointsAvailable.toLocaleString('ko-KR')}P)`);
                pointsInput.value = totalPointsAvailable;
            } else if (pointsToUse > (orderAmount + shippingFee - productDiscount)) {
                alert('포인트는 할인 후 결제 금액을 초과하여 사용할 수 없습니다.');
                pointsInput.value = orderAmount + shippingFee - productDiscount;
            } else {
                alert(`${pointsToUse.toLocaleString('ko-KR')}P가 적용되었습니다.`);
            }
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
    // 🚀 3. 최종 결제 버튼 로직
    // =======================================================
    const mainOrderBtn = document.getElementById('mainOrderBtn');
    if (mainOrderBtn) {
        mainOrderBtn.addEventListener('click', function(event) {
            event.preventDefault();

            if (confirm('결제를 진행하시겠습니까?')) {
                const usedPoints = parseInt(document.getElementById('pointsToUseInput').value, 10) || 0;
                const usedCouponId = document.getElementById('couponSelect').value;

                const memberDTO = {
                    mem_name: document.getElementById('recipientName').value,
                    mem_hp: document.getElementById('recipientHp').value,
                    mem_zip: document.getElementById('mem_zip').value,
                    mem_addr1: document.getElementById('mem_addr1').value,
                    mem_addr2: document.getElementById('mem_addr2_input').value
                };

                const orderItems = []; // cartItems -> orderItems로 이름 변경 (의미 명확화)
                document.querySelectorAll('.order-item').forEach(item => {
                    // cartId 대신 상품의 상세 정보를 직접 읽어옴
                    const prodNo = item.dataset.prodNo;
                    const quantity = item.dataset.quantity;
                    const option = item.dataset.option;

                    // cart_id를 보내는 대신, 상품 정보를 직접 보냄
                    orderItems.push({
                        prod_no: prodNo,
                        cart_item_cnt: parseInt(quantity, 10),
                        cart_option: option,

                        // cart_id는 장바구니에서 넘어온 경우에만 존재하므로, 있을 때만 추가
                        cart_id: item.dataset.cartId ? parseInt(item.dataset.cartId, 10) : null
                    });
                });

                const finalPaymentText = document.getElementById('summaryFinalPayment').textContent;
                const finalPaymentAmount = parseInt(finalPaymentText.replace(/[^0-9]/g, ''), 10);

                const checkedPaymentInput = document.querySelector('input[name="payment"]:checked');
                let paymentMethod = '';
                if (checkedPaymentInput) {
                    paymentMethod = checkedPaymentInput.parentElement.querySelector('strong').textContent.trim();
                }

                const payload = {
                    usedPoints: usedPoints,
                    usedCouponId: usedCouponId,
                    memberDTO: memberDTO,
                    orderItems: orderItems, // 수정된 orderItems 배열 사용
                    paymentMethod: paymentMethod,
                    finalAmount: finalPaymentAmount
                };

                fetch('/shoply/api/product/order', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(payload)
                })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('결제 처리 중 서버 오류가 발생했습니다.');
                        }
                        return response.json();
                    })
                    .then(data => {
                        console.log('주문 성공:', data);

                        const params = new URLSearchParams();
                        params.append('orderId', data.orderId);
                        data.cartNoList.forEach(cartNo => {
                            params.append('cartNoList', cartNo);
                        });
                        if (data.cpCode) {
                            params.append('cpCode', data.cpCode);
                        }
                        params.append('usedPoint', data.usedPoint);

                        // 'payload' 객체를 만들 때 사용했던 paymentMethod 변수의 값을 URL 파라미터에 추가
                        params.append('payment', payload.paymentMethod);

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