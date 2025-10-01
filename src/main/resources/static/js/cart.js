document.addEventListener('DOMContentLoaded', function () {
    const selectAllCheckbox = document.getElementById('selectAllCheckbox');
    const itemCheckboxes = document.querySelectorAll('.item-checkbox');
    const deleteSelectedBtn = document.getElementById('deleteSelectedBtn');

    // --- 기능: 전체 선택 및 해제 ---
    selectAllCheckbox.addEventListener('change', function () {
        itemCheckboxes.forEach(checkbox => {
            checkbox.checked = this.checked;
        });
        updateSummary();
    });

    // --- 기능: 개별 상품 선택 시 '전체선택' 체크박스 상태 업데이트 ---
    itemCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', function () {
            const allChecked = Array.from(itemCheckboxes).every(cb => cb.checked);
            selectAllCheckbox.checked = allChecked;
            updateSummary();
        });
    });
    
    // --- 기능: 수량 조절 ---
    document.querySelectorAll('.quantity-selector').forEach(selector => {
        const downBtn = selector.querySelector('.quantity-down');
        const upBtn = selector.querySelector('.quantity-up');
        const input = selector.querySelector('.quantity-input');

        downBtn.addEventListener('click', function() {
            let currentValue = parseInt(input.value);
            if (currentValue > 1) {
                input.value = currentValue - 1;
                updateSummary();
            }
        });

        upBtn.addEventListener('click', function() {
            let currentValue = parseInt(input.value);
            input.value = currentValue + 1;
            updateSummary();
        });
    });

    // --- 기능: 선택 삭제 ---
    deleteSelectedBtn.addEventListener('click', function () {
        const checkedItems = document.querySelectorAll('.item-checkbox:checked');
        if(checkedItems.length === 0) {
            alert('삭제할 상품을 선택해주세요.');
            return;
        }
        if(confirm('선택한 상품을 장바구니에서 삭제하시겠습니까?')) {
            checkedItems.forEach(checkbox => {
                checkbox.closest('.cart-item').remove();
            });
            updateSummary();
        }
    });
    
    // --- 기능: 개별 상품 삭제 버튼 ---
    document.querySelectorAll('.delete-item').forEach(button => {
        button.addEventListener('click', function() {
            if(confirm('이 상품을 장바구니에서 삭제하시겠습니까?')) {
                this.closest('.cart-item').remove();
                updateSummary();
            }
        });
    });
    
    // --- 기능: 주문하기 버튼 클릭 시 확인 대화상자 ---
    document.getElementById('orderBtn').addEventListener('click', function() {
        const checkedItems = document.querySelectorAll('.item-checkbox:checked');
        if(checkedItems.length === 0) {
            alert('주문할 상품을 선택해주세요.');
            return;
        }
        if(confirm('선택한 상품을 주문하시겠습니까?')) {
            // 실제로는 주문 페이지로 이동하는 로직이 들어갑니다.
            alert('주문 페이지로 이동합니다.');
            // window.location.href = '/order-page-url';
        }
    });


    // --- 기능: 합계 계산 및 업데이트 ---
    function updateSummary() {
        let totalItemsCount = 0;
        let orderAmount = 0;
        let discountAmount = 0;
        const shippingFeeThreshold = 30000; // 3만원 이상 무료배송
        let baseShippingFee = 3000; 

        document.querySelectorAll('.cart-item').forEach(item => {
            const checkbox = item.querySelector('.item-checkbox');
            if (checkbox.checked) {
                const price = parseInt(item.dataset.price);
                const discount = parseInt(item.dataset.discount);
                const quantity = parseInt(item.querySelector('.quantity-input').value);
                
                totalItemsCount += quantity;
                orderAmount += price * quantity;
                discountAmount += discount * quantity;
            }
        });

        const shippingFee = (orderAmount - discountAmount >= shippingFeeThreshold || orderAmount === 0) ? 0 : baseShippingFee;
        const totalPayment = orderAmount - discountAmount + shippingFee;
        const points = Math.floor(totalPayment * 0.001); // 0.1% 적립 예시

        // 화면에 값 업데이트
        document.getElementById('totalItemsCount').textContent = totalItemsCount;
        document.getElementById('orderAmount').textContent = orderAmount.toLocaleString() + ' 원';
        document.getElementById('discountAmount').textContent = '- ' + discountAmount.toLocaleString() + ' 원';
        document.getElementById('shippingFee').textContent = '+ ' + shippingFee.toLocaleString() + ' 원';
        document.getElementById('totalPayment').textContent = totalPayment.toLocaleString() + ' 원';
        document.getElementById('points').textContent = points.toLocaleString() + ' P';
    }

    // 페이지 로드 시 초기 계산 실행
    itemCheckboxes.forEach(cb => cb.checked = true);
    if(selectAllCheckbox) {
        selectAllCheckbox.checked = true;
    }
    updateSummary();
});