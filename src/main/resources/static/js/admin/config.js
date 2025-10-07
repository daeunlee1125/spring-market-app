document.addEventListener("DOMContentLoaded", function(){
    // ========== 인덱스 새로고침 함수 ==========
    function refreshIndexes() {
        document.querySelectorAll('.category-item-4 input[name^="mainCategories"]').forEach((input, idx) => {
            input.name = `mainCategories[${idx}].name`;
        });

        document.querySelectorAll('.subcategory-item-4 input[name^="subCategories"]').forEach((input, idx) => {
            input.name = `subCategories[${idx}].name`;
        });
    }




    const tabMenu2 = document.querySelector("#tabMenu-2")
    if (tabMenu2) {
        tabMenu2.querySelectorAll("li").forEach(tab => {
            tab.addEventListener("click", function() {
                // 1. 모든 탭 콘텐츠 숨기기
                document.querySelectorAll(".tab-content-2").forEach(c => c.style.display = "none");

                // 2. 모든 탭 비활성화
                document.querySelectorAll("#tabMenu-2 li").forEach(li => li.classList.remove("active"));

                // 3. 선택한 탭만 표시
                document.getElementById(this.dataset.target).style.display = "block";

                // 4. 탭 강조
                this.classList.add("active");
            });
        });
    }




    document.addEventListener("click", function(e) {
        // 삭제 버튼 누르면 토글 방지
        if (e.target.classList.contains("delete-btn-4")) return;

        // category-header-4 내부 클릭 잡기
        const header = e.target.closest(".category-header-4");
        if (header) {
            const sub = header.parentElement.querySelector(".subcategory-4");
            const icon = header.querySelector(".toggle-btn-4");
            if (sub) {
                sub.classList.toggle("active");
                icon.textContent = sub.classList.contains("active") ? "▼" : "▶";
            }
        }
    });




    let dragging = null;

// 공통 dragstart/dragend
    document.addEventListener("dragstart", e => {
        if (e.target.matches(".category-item-4, .subcategory-item-4")) {
            dragging = e.target;
            e.target.classList.add("dragging-4");
        }
    });

    document.addEventListener("dragend", e => {
        if (dragging) dragging.classList.remove("dragging-4");
        dragging = null;
    });

// dragover
    document.addEventListener("dragover", e => {
        e.preventDefault();
        if (!dragging) return;

        // =========================
        // 메인 카테고리 이동
        // =========================
        if (dragging.matches(".category-item-4") && !dragging.querySelector(".add-main")) {
            const mainList = document.querySelector(".category-list-4");

            // "+ 1차카테고리추가" li 제외
            const mainItems = [...mainList.querySelectorAll(".category-item-4")]
                .filter(li => !li.querySelector(".add-main"));

            const after = getDragAfterElement(mainList, e.clientY, ".category-item-4", true);

            const lastLi = mainList.lastElementChild; // "+ 1차카테고리추가" li

            if (after == null) {
                mainList.insertBefore(dragging, lastLi);
            } else {
                mainList.insertBefore(dragging, after);
            }
        }

        // =========================
        // 서브 카테고리 이동 (같은 1차 내에서만)
        // =========================
        if (dragging.matches(".subcategory-item-4")) {
            const originMain = dragging.closest(".category-item-4");
            const mainTarget = e.target.closest(".category-item-4");

            if (!mainTarget || mainTarget !== originMain) return; // 다른 1차로 이동 금지

            const subList = mainTarget.querySelector(".subcategory-4");
            subList.classList.add("active");

            const after = getDragAfterElement(subList, e.clientY, ".subcategory-item-4", false);

            if (after == null) {
                const addButtonLi = subList.lastElementChild; // "+ 2차카테고리추가" li
                subList.insertBefore(dragging, addButtonLi);
            } else {
                subList.insertBefore(dragging, after);
            }
        }



    });


    // =========================
    // 위치 계산 함수
    // =========================
    function getDragAfterElement(container, y, selector, excludeAddBtn) {
        const els = [...container.querySelectorAll(selector)]
            .filter(el => !el.classList.contains("dragging-4"))
            .filter(el => !excludeAddBtn || !el.querySelector(".add-main")); // 마지막 li 제외

        let closest = { offset: Number.NEGATIVE_INFINITY, element: null };

        els.forEach(child => {
            const box = child.getBoundingClientRect();
            const offset = y - box.top - box.height / 2;
            if (offset < 0 && offset > closest.offset) {
                closest = { offset, element: child };
            }
        });

        return closest.element;
    }


    document.getElementById("cateForm").addEventListener("submit", function(e) {
        const mainItems = document.querySelectorAll(".category-item-4:not(:last-child)");

        let mainIndex = 0;
        let subIndex = 0;
        let cate1_no_cnt = 1;
        let cate2_no_cnt = 1;

        mainItems.forEach(mainItem => {
            const header = mainItem.querySelector(".category-header-4");
            const mainNameInput = header.querySelector('input[name^="mainCategories"][name$=".cate1_name"]');

            let mainNoInput = header.querySelector('input[name^="mainCategories"][name$=".cate1_no"]');
            if (!mainNoInput) {
                mainNoInput = document.createElement("input");
                mainNoInput.type = "hidden";
                header.appendChild(mainNoInput);
            }

            // 1차 인덱스/번호 재할당
            if (mainNameInput) mainNameInput.name = `mainCategories[${mainIndex}].cate1_name`;
            mainNoInput.name = `mainCategories[${mainIndex}].cate1_no`;
            mainNoInput.value = cate1_no_cnt++;

            // 하위 2차들 처리
            const subItems = mainItem.querySelectorAll(".subcategory-item-4");
            subItems.forEach(subItem => {
                const subNameInput = subItem.querySelector('input[name^="subCategories"][name$=".cate2_name"]');

                let cate2NoInput = subItem.querySelector('input[name^="subCategories"][name$=".cate2_no"]');
                if (!cate2NoInput) {
                    cate2NoInput = document.createElement("input");
                    cate2NoInput.type = "hidden";
                    subItem.appendChild(cate2NoInput);
                }

                let cate1NoInput = subItem.querySelector('input[name^="subCategories"][name$=".cate1_no"]');
                if (!cate1NoInput) {
                    cate1NoInput = document.createElement("input");
                    cate1NoInput.type = "hidden";
                    subItem.appendChild(cate1NoInput);
                }

                if (subNameInput) subNameInput.name = `subCategories[${subIndex}].cate2_name`;

                cate2NoInput.name = `subCategories[${subIndex}].cate2_no`;
                cate2NoInput.value = cate2_no_cnt++;

                // ★ 외래키를 "현재 li의 최신 cate1_no"로 재매핑
                cate1NoInput.name = `subCategories[${subIndex}].cate1_no`;
                cate1NoInput.value = mainNoInput.value;

                subIndex++;
            });

            mainIndex++;
        });
    });






    const categoryList = document.querySelector('.category-list-4');

    if (categoryList) {
        // ========== 1차 카테고리 추가 ==========
        categoryList.addEventListener('click', function (e) {
            if (e.target.classList.contains('add-main')) {
                e.preventDefault();
                const name = prompt('추가할 1차 카테고리명을 입력하세요:');
                if (!name) return;

                const li = document.createElement('li');
                li.className = 'category-item-4';
                li.setAttribute('draggable', 'true');

                li.innerHTML = `
          <div class="category-header-4">
            <span class="toggle-btn-4">▶</span> ${name}
            <input type="hidden" name="mainCategories[].cate1_name" value="${name}">
            <button type="button" class="delete-btn-4">삭제</button>
          </div>
          <ul class="subcategory-4">
            <li>
              <button type="button" class="add-btn-4 add-sub">+ 2차카테고리추가</button>
            </li>
          </ul>
        `;

                // "1차카테고리추가" 버튼 li 앞에 삽입
                categoryList.insertBefore(li, categoryList.lastElementChild);
            }
        });

        // ========== 2차 카테고리 추가 ==========

        categoryList.addEventListener("click", function (e) {
            if (e.target.classList.contains("add-sub")) {
                e.preventDefault();

                const name = prompt("추가할 2차 카테고리명을 입력하세요:");
                if (!name) return;

                const mainItem = e.target.closest(".category-item-4");
                if (!mainItem) return;

                const mainNoInput = mainItem.querySelector('input[name^="mainCategories"][name$=".cate1_no"]');
                const cate1_no_value = mainNoInput ? mainNoInput.value : "";

                const subLi = document.createElement("li");
                subLi.className = "subcategory-item-4";
                subLi.setAttribute("draggable", "true");
                subLi.innerHTML = `
            ${name}
            <input type="hidden" name="subCategories.cate2_name" value="${name}">
            <input type="hidden" name="subCategories.cate1_no" value="${cate1_no_value}">
            <button type="button" class="delete-btn-4">삭제</button>
        `;

                const subUl = e.target.closest("ul");
                subUl.insertBefore(subLi, subUl.lastElementChild);
            }
        });


        // ========== 카테고리 삭제 ==========
        categoryList.addEventListener('click', function (e) {
            if (e.target.classList.contains('delete-btn-4')) {
                e.preventDefault();
                if (confirm('정말 삭제하시겠습니까?')) {
                    e.target.closest('li').remove();
                }
            }
        });
    }





    // --- 모달 로직 ---
    const openModalBtn = document.getElementById('openModalBtn');
    const closeModalBtn = document.getElementById('closeModalBtn');
    const modal = document.getElementById('modal');



    if (openModalBtn && closeModalBtn && modal) {
        openModalBtn.addEventListener('click', () => modal.classList.remove('hidden'));
        closeModalBtn.addEventListener('click', () => modal.classList.add('hidden'));
        modal.addEventListener('click', (event) => {
            if (event.target === modal) modal.classList.add('hidden');
        });

    }

    // --- 확인 모달 로직 ---
    const openModalBtn2 = document.getElementsByClassName('openModalBtn2');
    const closeModalBtn2 = document.getElementById('closeModalBtn2');
    const closeModalBtn2_1 = document.getElementById('closeModalBtn2-1');
    const modal2 = document.getElementById('modal2');

    const modalVersion = document.getElementById('modalVersion');
    const modalDescription = document.getElementById('modalDescription');

    if (modal2) {
        // '확인' 버튼들을 클릭하면 모달을 보여줍니다.
        if (openModalBtn2.length > 0) {
            for (const btn of openModalBtn2) {
                btn.addEventListener('click', (e) => {
                    modalVersion.textContent = e.target.dataset.version;
                    modalDescription.innerHTML = e.target.dataset.description.replace(/\n/g, '<br>');
                    modal2.classList.remove('hidden');
                });
            }
        }

        // 'x' 버튼
        if (closeModalBtn2) {
            closeModalBtn2.addEventListener('click', () => {
                modal2.classList.add('hidden');
            });
        }

        // '닫기' 버튼
        if (closeModalBtn2_1) {
            closeModalBtn2_1.addEventListener('click', () => {
                modal2.classList.add('hidden');
            });
        }

        // 배경 클릭 시 닫기
        modal2.addEventListener('click', (event) => {
            if (event.target === modal2) {
                modal2.classList.add('hidden');
            }
        });
    }

    // --- Daum 우편번호 서비스 로직 ---
    const searchAddressBtn = document.getElementById('searchAddressBtn');

    function execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분입니다.
                let addr = ''; // 주소 변수

                // 사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져옵니다.
                if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                    addr = data.roadAddress;
                } else { // 사용자가 지번 주소를 선택했을 경우(J)
                    addr = data.jibunAddress;
                }

                // 우편번호와 주소 정보를 해당 필드에 넣습니다.
                document.getElementById('zipcode').value = data.zonecode;
                document.getElementById("address").value = addr;
                // 커서를 상세주소 필드로 이동시킵니다.
                document.getElementById("address-detail").focus();
            }
        }).open();
    }

    // '우편번호 검색' 버튼 클릭 시 함수를 실행합니다.
    if (searchAddressBtn){
        searchAddressBtn.addEventListener('click', execDaumPostcode);
    }
})