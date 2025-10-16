document.addEventListener('DOMContentLoaded', function () {
    const selectAllCheckbox = document.getElementById('selectAllCheckbox');
    const itemCheckboxes = document.querySelectorAll('.item-checkbox');
    const deleteSelectedBtn = document.getElementById('deleteSelectedBtn');

    // --- 기능: 전체 선택 및 해제 ---
    if (selectAllCheckbox) {
        selectAllCheckbox.addEventListener('change', function () {
            itemCheckboxes.forEach(checkbox => {
                checkbox.checked = this.checked;
            });
            updateSummary();
        });
    }

    // --- 기능: 개별 상품 선택 시 '전체선택' 체크박스 상태 업데이트 ---
    if (itemCheckboxes) {
        itemCheckboxes.forEach(checkbox => {
            checkbox.addEventListener('change', function () {
                const allChecked = Array.from(itemCheckboxes).every(cb => cb.checked);
                selectAllCheckbox.checked = allChecked;
                updateSummary();
            });
        });
    }

    // --- 기능: 수량 조절 ---
    document.querySelectorAll('.quantity-selector').forEach(selector => {
        const downBtn = selector.querySelector('.quantity-down');
        const upBtn = selector.querySelector('.quantity-up');
        const input = selector.querySelector('.quantity-input');

        // ✅ async 키워드 추가: 비동기 작업(fetch)을 하기 위함
        downBtn.addEventListener('click', async function() {
            let currentValue = parseInt(input.value);
            if (currentValue > 1) {
                const newValue = currentValue - 1;

                // ✅ 서버에 업데이트 요청 후 성공했을 때만 화면 변경
                const cartItem = this.closest('.cart-item');
                const cart_no = cartItem.querySelector('input[name="cart_no"]').value;

                // 서버 업데이트 함수 호출
                const isSuccess = await updateCartOnServer(cart_no, newValue);

                if (isSuccess) {
                    input.value = newValue; // 성공 시 화면 값 변경
                    updateItemPrice(input);
                    updateSummary();
                }
            }
        });

        // ✅ async 키워드 추가
        upBtn.addEventListener('click', async function() {
            let currentValue = parseInt(input.value);
            const newValue = currentValue + 1;

            // ✅ 서버에 업데이트 요청 후 성공했을 때만 화면 변경
            const cartItem = this.closest('.cart-item');
            const cart_no = cartItem.querySelector('input[name="cart_no"]').value;

            // 서버 업데이트 함수 호출
            const isSuccess = await updateCartOnServer(cart_no, newValue);

            if (isSuccess) {
                input.value = newValue; // 성공 시 화면 값 변경
                updateItemPrice(input);
                updateSummary();
            }
        });
    });

    /**
     * ✅ [새로운 함수] 서버에 장바구니 수량 업데이트를 요청합니다.
     * @param {number} cart_no - 장바구니 번호
     * @param {number} quantity - 새로운 수량
     * @returns {Promise<boolean>} - 성공 여부
     */
    async function updateCartOnServer(cart_no, quantity) {
        try {
            const response = await fetch('/shoply/product/cart/update', { // 수량 업데이트를 처리할 URL
                method: 'PATCH', // 일부 리소스만 수정할 때는 PATCH가 의미상 더 적합합니다.
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    cart_no: cart_no,
                    cart_item_cnt: quantity
                })
            });

            if (!response.ok) {
                // 서버에서 2xx 응답을 주지 않으면 에러로 처리
                throw new Error('서버 응답 오류: ' + response.statusText);
            }

            console.log('장바구니 수량 DB 업데이트 성공!');
            return true; // 성공 시 true 반환

        } catch (error) {
            console.error('장바구니 수량 업데이트 실패:', error);
            alert('수량 변경에 실패했습니다. 잠시 후 다시 시도해주세요.');
            return false; // 실패 시 false 반환
        }
    }

    /**
     * ✅ 새로 만든 함수: 특정 상품의 수량에 맞춰 가격을 업데이트합니다.
     * @param {HTMLInputElement} quantityInput - 수량이 변경된 input 요소
     */
    function updateItemPrice(quantityInput) {
        // 1. input 요소를 기준으로 관련된 다른 요소들을 찾습니다.
        const cartItem = quantityInput.closest('.cart-item');
        const priceElement = cartItem.querySelector('.cart-item-price');
        const basePrice = parseInt(cartItem.dataset.price, 10); // 상품 단가

        // 2. 현재 수량과 단가를 곱해 새 가격을 계산합니다.
        const newQuantity = parseInt(quantityInput.value, 10);
        const newTotalPrice = basePrice * newQuantity;

        // 3. 계산된 가격을 화면에 콤마를 찍어 표시합니다.
        priceElement.textContent = newTotalPrice.toLocaleString('ko-KR') + '원';
    }

    // --- 기능: 선택 삭제 ---
    if (deleteSelectedBtn) {
        // async 키워드를 추가하여 비동기 함수로 만듭니다.
        deleteSelectedBtn.addEventListener('click', async function () {
            // 1. 삭제할 아이템의 cart_no를 수집합니다.
            const checkedItems = document.querySelectorAll('.item-checkbox:checked');

            if (checkedItems.length === 0) {
                alert('삭제할 상품을 선택해주세요.');
                return;
            }

            const cart_no_list = [];
            checkedItems.forEach(checkbox => {
                // 각 체크박스에서 가장 가까운 .cart-item 부모 요소를 찾습니다.
                const cartItem = checkbox.closest('.cart-item');
                // 그 안에서 name이 'cart_no'인 input의 값을 찾아 배열에 추가합니다.
                const cart_no = cartItem.querySelector('input[name="cart_no"]').value;
                cart_no_list.push(cart_no);
            });

            // 2. 서버에 삭제 요청을 보냅니다.
            if (confirm('선택한 상품을 장바구니에서 삭제하시겠습니까?')) {
                try {
                    const response = await fetch('/shoply/product/delete/selected', { // 다건 삭제를 처리할 URL
                        method: 'DELETE', // DELETE 메서드 사용
                        headers: {
                            'Content-Type': 'application/json' // 전송하는 데이터가 JSON임을 명시
                        },
                        body: JSON.stringify(cart_no_list) // JavaScript 배열을 JSON 문자열로 변환하여 전송
                    });

                    if (!response.ok) { // HTTP 상태 코드가 2xx가 아닐 경우
                        throw new Error('서버 응답이 올바르지 않습니다.');
                    }

                    // 3. 요청 성공 시 화면에서 아이템을 제거하고 요약 정보를 업데이트합니다.
                    alert('선택한 상품이 삭제되었습니다.');
                    checkedItems.forEach(checkbox => {
                        checkbox.closest('.cart-item').remove();
                    });
                    updateSummary();

                } catch (error) {
                    console.error('삭제 중 오류 발생:', error);
                    alert('상품 삭제 중 오류가 발생했습니다.');
                }
            }
        });
    }

    // // --- 기능: 주문하기 버튼 클릭 시 확인 대화상자 ---
    // document.getElementById('orderBtn').addEventListener('click', function() {
    //     const checkedItems = document.querySelectorAll('.item-checkbox:checked');
    //     if(checkedItems.length === 0) {
    //         alert('주문할 상품을 선택해주세요.');
    //         return;
    //     }
    //     if(confirm('선택한 상품을 주문하시겠습니까?')) {
    //         // 실제로는 주문 페이지로 이동하는 로직이 들어갑니다.
    //         alert('주문 페이지로 이동합니다.');
    //         // window.location.href = '/order-page-url';
    //     }
    // });


    // --- 기능: 합계 계산 및 업데이트 ---
    function updateSummary() {
        let totalItemsCount = 0; // 총 수량
        let orderAmount = 0;     // 주문금액 (할인 전 총액)
        let discountAmount = 0;  // 총 상품할인 금액
        let totalShippingFee = 0; // 총 배송비
        let totalPayment = 0;     // 결제예정금액
        let totalpoint = 0;       // 적립포인트
        let totalprice = 0;

        // 모든 장바구니 아이템들을 순회
        document.querySelectorAll('.cart-item').forEach(item => {
            const checkbox = item.querySelector('.item-checkbox');

            // 체크된 아이템만 계산에 포함
            if (checkbox.checked) {
                // 1. HTML의 데이터 속성에서 필요한 값들을 가져옴
                const price = parseInt(item.dataset.price, 10); // 원가 (prod_price)
                const saleRate = parseInt(item.dataset.saleRate, 10); // 할인율 (prod_sale)
                const deliveryFee = parseInt(item.dataset.deliveryFee, 10); // 개별 배송비 (prod_deliv_price)
                const realPrice = parseInt(item.dataset.realPrice, 10); // 실 판매가 (realPrice)
                const quantity = parseInt(item.querySelector('.quantity-input').value, 10); // 현재 수량
                const point = parseInt(item.dataset.point, 10); // 적립 포인트(prod_point)

                // 2. DTO 로직에 맞춰 각 항목을 계산하고 누적
                totalItemsCount += quantity;
                orderAmount += price * quantity; // 주문금액 = 원가 * 수량
                totalpoint += point * quantity;

                // 상품할인 = (원가 * 할인율 / 100) * 수량
                // 소수점 계산이 있을 수 있으므로 Math.floor로 버림 처리
                discountAmount += Math.floor((price * saleRate / 100)) * quantity;

                totalShippingFee += deliveryFee; // 배송비는 개별 배송비를 모두 더함

                // 결제예정금액 = (실 판매가 * 수량)의 합
                totalPayment += realPrice * quantity;
            }
        });

        // 최종 결제예정금액 = (모든 상품의 실판매가 * 수량) + 총 배송비
        const finalPayment = totalPayment + totalShippingFee;

        // 3. 계산된 최종 값들을 화면에 업데이트
        document.getElementById('totalItemsCount').textContent = totalItemsCount;
        document.getElementById('orderAmount').textContent = orderAmount.toLocaleString('ko-KR') + ' 원';
        document.getElementById('discountAmount').textContent = '- ' + discountAmount.toLocaleString('ko-KR') + ' 원';
        document.getElementById('shippingFee').textContent = '+ ' + totalShippingFee.toLocaleString('ko-KR') + ' 원';
        document.getElementById('totalPayment').textContent = finalPayment.toLocaleString('ko-KR') + ' 원';
        document.getElementById('points').textContent = totalpoint.toLocaleString('ko-KR') + ' P';
    }

    // 페이지 로드 시 초기 계산 실행
    if (itemCheckboxes) {
        itemCheckboxes.forEach(cb => cb.checked = true);
    }
    if(selectAllCheckbox) {
        selectAllCheckbox.checked = true;
    }
    updateSummary();

   // 주문하기
   document.getElementById('orderBtn').addEventListener('click', function(e) {
       e.preventDefault(); // 버튼 기본 동작 막기

       const form = document.getElementById('orderForm');
       const checkedItems = document.querySelectorAll('.item-checkbox:checked');
       if(checkedItems.length === 0) {
           alert('주문할 상품을 선택해주세요.');
           return;
       }

       if(confirm('선택한 상품을 주문하시겠습니까?')) {
           // 폼 안의 모든 체크박스 비활성화
           form.querySelectorAll('.item-checkbox').forEach(cd => {
               cd.disabled = true;
           })

           // 체크된 체크박스만 다시 활성화
           // => 이렇게 하면 폼이 전송될 때 체크된 상품의 cart_no 값만 서버로 넘어감
           checkedItems.forEach(checkbox => {
               checkbox.disabled = false;
           })

           // 폼 전송
           form.submit();
       }
   })


});