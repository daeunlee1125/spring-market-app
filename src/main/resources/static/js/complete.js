document.addEventListener('DOMContentLoaded', function() {
    // JavaScript 기능이 필요한 경우 여기에 작성
    // 예: 버튼 클릭 시 페이지 이동
    const continueShoppingBtn = document.querySelector('.btn-secondary');
    const orderDetailsBtn = document.querySelector('.btn-primary');

    if (continueShoppingBtn) {
        continueShoppingBtn.addEventListener('click', () => {
            // 메인 페이지로 이동
            window.location.href = '/kmarket/index.html'; 
        });
    }

    if (orderDetailsBtn) {
        orderDetailsBtn.addEventListener('click', () => {
            // 주문 상세 페이지로 이동 (실제로는 주문번호를 포함한 URL로 이동)
            alert('주문 상세 페이지로 이동합니다.');
            // window.location.href = '/kmarket/my/orderDetails?orderId=202509280952869';
        });
    }
});