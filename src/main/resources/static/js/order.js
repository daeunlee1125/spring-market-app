document.addEventListener('DOMContentLoaded', function () {
    
    // 결제 버튼 이벤트 핸들러 함수
    const handleOrderClick = function(event) {
        // 실제로는 약관 동의 등을 확인하는 로직이 필요합니다.
        if (confirm('결제를 진행하시겠습니까?')) {
            alert('결제가 완료되었습니다.');
            // 실제 결제 처리 페이지로 이동
            // window.location.href = '/payment-complete';
        }
    };

    // 메인 컨텐츠의 결제 버튼
    const mainOrderBtn = document.getElementById('mainOrderBtn');
    if(mainOrderBtn) {
        mainOrderBtn.addEventListener('click', handleOrderClick);
    }

    // 사이드바의 결제 버튼
    const sideOrderBtn = document.getElementById('sideOrderBtn');
    if(sideOrderBtn) {
        sideOrderBtn.addEventListener('click', handleOrderClick);
    }
    
});