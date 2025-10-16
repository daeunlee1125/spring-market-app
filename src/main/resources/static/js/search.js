document.addEventListener('DOMContentLoaded', function () {
    const secondarySearchForm = document.getElementById('secondarySearchForm');
    const sortLinks = document.querySelectorAll('.sort-link');

    // 페이지의 기본 검색어와 현재 정렬 상태를 data 속성에서 읽어옴
    const mainKeywordElement = document.querySelector('.main-list-headName');
    const sortContainer = document.querySelector('.sort-container');

    // 방어 코드: 요소가 없을 경우를 대비하여 기본값 설정
    const mainKeyword = mainKeywordElement ? mainKeywordElement.dataset.keyword : '';
    const currentSort = sortContainer ? sortContainer.dataset.sort : 'sold';

    // 1. 상세 검색(2차 검색) Form 제출 시
    if (secondarySearchForm) {
        secondarySearchForm.addEventListener('submit', function (event) {
            event.preventDefault(); // 기본 제출 동작 중단

            const formData = new URLSearchParams(new FormData(this));

            // 💡 핵심: 1차 검색어였던 mainKeyword를 'keyword'라는 이름으로 파라미터에 추가
            formData.append('keyword', mainKeyword);

            // 정렬 파라미터 추가
            formData.append('sort', currentSort);

            const finalUrl = `/shoply/product/search?${formData.toString()}`;

            // 디버깅을 위해 최종 URL을 콘솔에 출력
            console.log('상세 검색 요청 URL:', finalUrl);

            window.location.href = finalUrl;
        });
    }

    // 2. 정렬 링크 클릭 시
    sortLinks.forEach(link => {
        link.addEventListener('click', function (event) {
            event.preventDefault(); // 기본 링크 이동 중단

            const formData = new URLSearchParams(new FormData(secondarySearchForm));

            // 💡 핵심: 1차 검색어 유지
            formData.append('keyword', mainKeyword);

            // 클릭된 링크의 정렬 값으로 변경
            const newSort = this.dataset.sort;
            formData.append('sort', newSort);

            const finalUrl = `/shoply/product/search?${formData.toString()}`;

            // 디버깅을 위해 최종 URL을 콘솔에 출력
            console.log('정렬 변경 요청 URL:', finalUrl);

            window.location.href = finalUrl;
        });
    });
});