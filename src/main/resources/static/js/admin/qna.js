document.addEventListener("DOMContentLoaded", function() {
    // 'clickable-row' 클래스를 가진 모든 <tr> 요소를 가져옵니다.
    const rows = document.querySelectorAll('.clickable-row');

    rows.forEach(row => {
        row.addEventListener('click', function(event) {
            // 💡 중요: 체크박스나 input을 클릭했을 때는 행 클릭이 동작하지 않도록 예외처리
            if (event.target.tagName === 'INPUT' || event.target.tagName === 'BUTTON') {
                return;
            }
            // 클릭된 행(row) 내부의 form을 찾습니다.
            const form = this.querySelector('form');
            // form이 존재하면 전송(submit)합니다.
            if (form) {
                form.submit();
            }
        });
    });

    // --- 1. 1차/2차 카테고리 동적 변경 및 필터링 기능 ---
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
    const searchBar = document.querySelector('.search-bar');

    // searchBar 요소가 있는 list.html에서만 아래 로직 전체를 실행
    if (searchBar) {
        const currentCate1 = searchBar.dataset.cate1;
        const currentCate2 = searchBar.dataset.cate2;

        function updateCategory2(selectedCategory1) {
            category2Select.innerHTML = '<option value="">2차 선택</option>';
            if (selectedCategory1 && categories[selectedCategory1]) {
                categories[selectedCategory1].forEach(function(subcategory) {
                    const option = document.createElement('option');
                    option.value = subcategory;
                    option.textContent = subcategory;
                    category2Select.appendChild(option);
                });
            }
        }

        if (currentCate1) {
            updateCategory2(currentCate1);
            category2Select.value = currentCate2;
        }

        // 카테고리 선택 시 필터링 기능
        function triggerCategorySearch() {
            const cate1 = category1Select.value;
            const cate2 = category2Select.value;
            const params = new URLSearchParams();
            if (cate1) params.append('cate1', cate1);
            if (cate2) params.append('cate2', cate2);
            const queryString = params.toString();
            // 💡 중요: 컨텍스트 경로가 있다면 `/shoply`를 포함해야 하고, 없다면 `/admin...` 으로 시작해야 합니다.
            const finalUrl = `/admin/cs/qna/list${queryString ? '?' + queryString : ''}`;
            window.location.href = finalUrl;
        }

        category1Select.addEventListener('change', triggerCategorySearch);
        category2Select.addEventListener('change', triggerCategorySearch);
    }

    // --- 2. 모달(팝업) 기능 ---
    document.querySelectorAll(".openModalBtn").forEach(btn => {
        btn.addEventListener("click", (e) => {
            e.preventDefault();
            const targetModalId = btn.dataset.target;
            const modal = document.getElementById(targetModalId);
            if (modal) {
                modal.classList.remove("hidden");
            }
        });
    });

    document.querySelectorAll(".modal, .modal-overlay").forEach(modal => {
        modal.addEventListener("click", (e) => {
            if (e.target.classList.contains("closeModalBtn") || e.target === modal) {
                modal.classList.add("hidden");
            }
        });
    });

    // --- 4. 삭제 기능 ---
    const confirmDeleteBtn = document.getElementById('confirmDeleteBtn');
    const removeViewBtn = document.getElementById('removeViewBtn');

    if (confirmDeleteBtn) {
        confirmDeleteBtn.addEventListener('click', function() {
            const checkedItems = document.querySelectorAll('input[name="itemCheck"]:checked');
            if (checkedItems.length === 0) {
                alert('삭제할 항목을 선택해주세요.');
                return;
            }
            const idsToDelete = Array.from(checkedItems).map(checkbox => checkbox.value);
            deleteQnaItems(idsToDelete);
        });
    }

    if (removeViewBtn) {
        removeViewBtn.addEventListener('click', function() {
            if (!confirm('해당 문의를 정말 삭제하시겠습니까?')) {
                return;
            }
            const idToDelete = [ this.dataset.delqnabtn ];
            deleteQnaItems(idToDelete, true);
        });
    }

    function deleteQnaItems(ids, isFromViewPage = false) {
        fetch('/shoply/admin/cs/qna/deleteSelected', { // 💡 컨텍스트 경로가 있다면 `/shoply`를 포함해야 합니다.
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(ids)
        })
            .then(response => {
                if (response.ok) {
                    alert('삭제가 완료되었습니다.');
                    if (isFromViewPage) {
                        window.location.href = '/shoply/admin/cs/qna/list'; // 💡 컨텍스트 경로 주의
                    } else {
                        window.location.reload();
                    }
                } else {
                    alert('삭제에 실패했습니다. 잠시 후 다시 시도해주세요.');
                }
            })
            .catch(error => {
                console.error('삭제 처리 중 네트워크 오류 발생:', error);
                alert('삭제 중 오류가 발생했습니다.');
            });
    }

    // --- 5. 문의하기 답변 등록 기능 ---
    const answerQnaBtn = document.getElementById('answerQna');

    if (answerQnaBtn) {
        answerQnaBtn.addEventListener('click', function() {
            // 'data-qna-no' 속성은 'qnaNo' 라는 이름으로 접근해야 합니다.
            const qNo = this.dataset.qnaNo;

            const contentTextarea = document.getElementById('qnaAnswerContent');
            const content = contentTextarea.value;

            // 3. 내용이 비어있는지 확인합니다.
            if (!content || content.trim() === '') {
                alert('답변 내용을 입력해주세요.');
                contentTextarea.focus(); // textarea에 포커스를 줍니다.
                return;
            }

            // 4. 서버로 보낼 JSON 데이터를 구성합니다.
            const dataToSend = {
                qNo: qNo,
                content: content
            };

            // 5. fetch API를 사용하여 서버에 POST 요청을 보냅니다.
            fetch('/shoply/admin/cs/qna/modifyAnswerQna', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(dataToSend)
            })
                .then(response => {
                    if (response.ok) {
                        alert('답변이 성공적으로 등록되었습니다.');
                        // 성공 시, 해당 게시글의 view 페이지로 이동합니다.
                        window.location.href = `/shoply/admin/cs/qna/view?qNo=${qNo}`;
                    } else {
                        // 서버에서 에러 응답(4xx, 5xx)이 왔을 때
                        alert('답변 등록에 실패했습니다. 다시 시도해주세요.');
                    }
                })
                .catch(error => {
                    console.error('답변 등록 중 네트워크 오류 발생:', error);
                    alert('답변 등록 중 오류가 발생했습니다.');
                });
        });
    }
});