document.addEventListener("DOMContentLoaded", function() {

    // --- 1. 1차/2차 카테고리 동적 변경 기능 ---
    const categories = {
        '회원': ['가입', '탈퇴', '회원정보', '로그인'],
        '쿠폰/혜택/이벤트': ['쿠폰/할인혜택', '포인트', '제휴', '이벤트'],
        '주문/결제': ['상품', '결제', '구매내역', '영수증/증빙'],
        '배송': ['배송상태/기간', '배송정보확인/변경', '해외배송', '당일배송', '해외직구'],
        '취소/반품/교환': ['반품신청/철회', '반품정보확인/변경', '교환 AS신청/철회', '교환정보확인/변경', '취소신청/철회', '취소확인/환불정보'],
        '여행/숙박/항공': ['여행/숙박', '항공'],
        '안전거래': ['서비스 이용규칙 위반', '지식재산권침해', '법령 및 정책위반 상품', '게시물 정책위반', '직거래/외부거래유도', '표시광고', '청소년 위해상품/이미지']
    };

    const category1Select = document.getElementById('category1');
    const category2Select = document.getElementById('category2');

    // HTML의 data-* 속성에서 현재 선택된 카테고리 값을 가져옴
    const searchBar = document.querySelector('.search-bar');
    const currentCate1 = searchBar.dataset.cate1;
    const currentCate2 = searchBar.dataset.cate2;

    /**
     * 2차 카테고리 옵션을 업데이트하는 함수
     * @param {string} selectedCategory1 - 선택된 1차 카테고리 값
     */
    function updateCategory2(selectedCategory1) {
        category2Select.innerHTML = '<option value="">2차 선택</option>'; // 초기화
        if (selectedCategory1 && categories[selectedCategory1]) {
            categories[selectedCategory1].forEach(function(subcategory) {
                const option = document.createElement('option');
                option.value = subcategory;
                option.textContent = subcategory;
                category2Select.appendChild(option);
            });
        }
    }

    if (category1Select) {
        // ✅ 페이지가 처음 로드될 때 1차 카테고리가 선택되어 있다면, 2차 카테고리를 바로 채워줌
        if (currentCate1) {
            updateCategory2(currentCate1);
            // 2차 카테고리 값도 있다면 선택 상태로 만듦
            category2Select.value = currentCate2;
        }
    }

    // --- 2. 모달(팝업) 기능 ---
    // 모든 '모달 열기' 버튼(class="openModalBtn")에 이벤트 리스너를 추가
    document.querySelectorAll(".openModalBtn").forEach(btn => {
        btn.addEventListener("click", (e) => {
            e.preventDefault();
            const targetModalId = btn.dataset.target; // 버튼의 data-target 속성 값 (예: "confirmEndModal")
            const modal = document.getElementById(targetModalId);
            if (modal) {
                modal.classList.remove("hidden"); // 해당 id를 가진 모달의 'hidden' 클래스를 제거하여 보여줌
            }
        });
    });

    // 모든 모달의 닫기 기능(배경 클릭 포함)에 이벤트 리스너를 추가
    // HTML에서 모달의 가장 바깥쪽 요소에 class="modal-overlay" 또는 "modal"이 있어야 함
    document.querySelectorAll(".modal, .modal-overlay").forEach(modal => {
        modal.addEventListener("click", (e) => {
            // 닫기 버튼(class="closeModalBtn")을 클릭했거나,
            // 모달의 배경 부분을 직접 클릭했을 때
            if (e.target.classList.contains("closeModalBtn") || e.target === modal) {
                modal.classList.add("hidden"); // 'hidden' 클래스를 추가하여 모달을 숨김
            }
        });
    });


    // --- 3. 카테고리 선택 시 필터링 기능 ---
    if (category1Select && category2Select) {
        function triggerCategorySearch() {
            const cate1 = category1Select.value;
            const cate2 = category2Select.value;

            const params = new URLSearchParams();
            if (cate1) params.append('cate1', cate1);
            if (cate2) params.append('cate2', cate2);

            const queryString = params.toString();
            const finalUrl = `/shoply/admin/cs/qna/list${queryString ? '?' + queryString : ''}`;
            window.location.href = finalUrl;
        }

        // 1차 또는 2차 카테고리가 변경될 때 페이지 리로드(필터링) 실행
        category1Select.addEventListener('change', triggerCategorySearch);
        category2Select.addEventListener('change', triggerCategorySearch);
    }

    // --- 4. 선택 삭제 기능 ---
    const confirmDeleteBtn = document.getElementById('confirmDeleteBtn');

    if (confirmDeleteBtn) {
        confirmDeleteBtn.addEventListener('click', function() {
            // 1. 체크된 모든 체크박스를 선택
            const checkedItems = document.querySelectorAll('input[name="itemCheck"]:checked');

            if (checkedItems.length === 0) {
                alert('삭제할 항목을 선택해주세요.');
                return;
            }

            // 2. 체크된 항목들의 value(q_no)를 수집하여 리스트(배열)로 만듦
            const idsToDelete = [];
            checkedItems.forEach(checkbox => {
                idsToDelete.push(checkbox.value);
            });

            // 3. fetch API를 사용하여 서버에 데이터 전송
            fetch('/shoply/admin/cs/qna/deleteSelected', {
                method: 'POST', // 또는 'DELETE'
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(idsToDelete) // JavaScript 배열을 JSON 문자열로 변환
            })
                .then(response => {
                    if (response.ok) {
                        // 성공적으로 삭제되었을 때
                        alert('선택한 항목이 삭제되었습니다.');
                        window.location.reload(); // 페이지를 새로고침하여 변경사항 반영
                    } else {
                        // 서버에서 에러 응답이 왔을 때
                        alert('삭제에 실패했습니다.');
                    }
                })
                .catch(error => {
                    console.error('삭제 처리 중 에러 발생:', error);
                    alert('삭제 중 오류가 발생했습니다.');
                });
        });
    }
});