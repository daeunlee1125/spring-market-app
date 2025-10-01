//member_find
// 탭 전환 기능
document.addEventListener('DOMContentLoaded', function() {
    const tabButtons = document.querySelectorAll('.tab-button');
    const tabContents = document.querySelectorAll('.tab-content');

    tabButtons.forEach(button => {
        button.addEventListener('click', function() {
            const targetTab = this.getAttribute('data-tab');

            // 모든 탭 버튼 비활성화
            tabButtons.forEach(btn => btn.classList.remove('active'));
            // 현재 클릭된 탭 버튼 활성화
            this.classList.add('active');

            // 모든 탭 콘텐츠 숨기기
            tabContents.forEach(content => content.classList.remove('active'));
            // 해당 탭 콘텐츠 보이기
            document.getElementById(targetTab + '-tab').classList.add('active');
        });
    });
});

/*
// 탭 전환 기능
document.addEventListener('DOMContentLoaded', function() {
    const tabButtons = document.querySelectorAll('.tab-button');
    const tabContents = document.querySelectorAll('.tab-content');

    tabButtons.forEach(button => {
        button.addEventListener('click', function() {
            const targetTab = this.getAttribute('data-tab');

            // 모든 탭 버튼 비활성화
            tabButtons.forEach(btn => btn.classList.remove('active'));
            // 현재 클릭된 탭 버튼 활성화
            this.classList.add('active');

            // 모든 탭 콘텐츠 숨기기
            tabContents.forEach(content => content.classList.remove('active'));
            // 해당 탭 콘텐츠 보이기
            document.getElementById(targetTab + '-tab').classList.add('active');
        });
    });
});*/
