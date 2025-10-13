document.addEventListener('DOMContentLoaded', function() {

    // ==========================================================
    // 별점 렌더링을 재사용 가능하도록 함수로 변경
    // ==========================================================
    function renderStarRatings() {
        const allStarRatings = document.querySelectorAll('.star-rating');
        allStarRatings.forEach(ratingElement => {
            const score = parseFloat(ratingElement.dataset.score);
            if (!isNaN(score)) {
                const percentage = (score / 5.0) * 100;
                const starFill = ratingElement.querySelector('.star-rating-fill');
                if (starFill) {
                    starFill.style.width = percentage + '%';
                }
            }
        });
    }

    // 페이지 최초 로드 시 별점 렌더링 실행
    renderStarRatings();

    // ==========================================================
    // 1. 옵션 선택 및 가격 계산 기능
    // ==========================================================
    // 옵션 선택 select 박스가 있는지 확인
    const allSelectOptions = document.querySelectorAll('.info-options select.select-option');
    const grandTotalPriceEl = document.querySelector('#grand-total-price');

    // ==========================================================
    // CASE 1: 옵션이 있는 경우
    // ==========================================================
    if (allSelectOptions && allSelectOptions.length > 0) {
        // #selected-options-list 요소를 먼저 찾습니다.
        const selectedOptionsList = document.querySelector('#selected-options-list');

        // .final과 .product-name 대신 data-* 속성에서 값을 가져옵니다.
        const basePrice = parseInt(selectedOptionsList.dataset.price, 10);
        const productName = selectedOptionsList.dataset.name;

        function updateGrandTotalWithOptions() {
            let grandTotal = 0;
            const selectedItems = document.querySelectorAll('.selected-item');
            selectedItems.forEach(item => {
                const quantityInput = item.querySelector('.quantity-input');
                const quantity = parseInt(quantityInput.value, 10);
                const itemTotal = basePrice * quantity;
                const itemTotalPriceEl = item.querySelector('.item-total-price');
                itemTotalPriceEl.textContent = itemTotal.toLocaleString() + '원';
                grandTotal += itemTotal;
            });
            grandTotalPriceEl.textContent = grandTotal.toLocaleString() + '원';
        }

        function addSelectedItem() {
            let allOptionsSelected = true;
            const selectedValues = [];
            allSelectOptions.forEach(select => {
                if (select.value === '') {
                    allOptionsSelected = false;
                }
                selectedValues.push(select.value);
            });

            if (allOptionsSelected) {
                const optionText = `${productName} / ${selectedValues.join(' / ')}`;
                let isDuplicate = false;
                document.querySelectorAll('.selected-item .item-name').forEach(nameEl => {
                    if (nameEl.textContent === optionText) {
                        isDuplicate = true;
                    }
                });

                if (isDuplicate) {
                    alert('이미 추가된 옵션입니다.');
                } else {
                    const newItemHtml = `
                        <div class="selected-item" data-option-combo="${optionText}">
                            <span class="item-name">${optionText}</span>
                            <div class="quantity-counter">
                                <button type="button" class="btn-minus">-</button>
                                <input type="text" class="quantity-input" value="1" readonly>
                                <button type="button" class="btn-plus">+</button>
                            </div>
                            <span class="item-total-price">${basePrice.toLocaleString()}원</span>
                            <button type="button" class="btn-remove">×</button>
                        </div>`;
                    selectedOptionsList.insertAdjacentHTML('beforeend', newItemHtml);
                }

                allSelectOptions.forEach(select => {
                    select.value = '';
                });
                updateGrandTotalWithOptions();
            }
        }

        allSelectOptions.forEach(select => {
            select.addEventListener('change', addSelectedItem);
        });

        if (selectedOptionsList) {
            selectedOptionsList.addEventListener('click', function(event) {
                const target = event.target;
                if (target.tagName === 'BUTTON') {
                    const item = target.closest('.selected-item');
                    if (!item) return;
                    const quantityInput = item.querySelector('.quantity-input');
                    let quantity = quantityInput ? parseInt(quantityInput.value, 10) : 0;
                    if (target.classList.contains('btn-plus')) {
                        quantity++;
                    } else if (target.classList.contains('btn-minus')) {
                        if (quantity > 1) quantity--;
                    } else if (target.classList.contains('btn-remove')) {
                        item.remove();
                    }
                    if (quantityInput) quantityInput.value = quantity;
                    updateGrandTotalWithOptions();
                }
            });
        }
    // ==========================================================
    // CASE 2: 옵션이 없는 경우
    // ==========================================================
    }else {
        const noOptionProductEl = document.querySelector('#no-option-product');
        if (noOptionProductEl) {
            const basePrice = parseInt(noOptionProductEl.dataset.price, 10);
            const quantityController = noOptionProductEl.querySelector('.quantity-counter');
            const quantityInput = noOptionProductEl.querySelector('.quantity-input');

            // 옵션 없는 단일 상품의 총 가격을 업데이트하는 함수
            function updateGrandTotalForSingleItem() {
                const quantity = parseInt(quantityInput.value, 10);
                const total = basePrice * quantity;
                grandTotalPriceEl.textContent = total.toLocaleString() + '원';
            }

            quantityController.addEventListener('click', function (e) {
                let quantity = parseInt(quantityInput.value, 10);

                if (e.target.classList.contains('btn-plus')) {
                    quantity++;
                } else if (e.target.classList.contains('btn-minus')) {
                    if (quantity > 1) {
                        quantity--;
                    }
                }
                quantityInput.value = quantity;
                updateGrandTotalForSingleItem(); // 총 가격 업데이트
            });
        }
    }

    // ==========================================================
    // 2. 상세정보 펼치기/접기 기능 (기존과 동일)
    // ==========================================================
    const toggleDetailBtn = document.querySelector('.toggle-detail-btn');
    if (toggleDetailBtn) {
        toggleDetailBtn.addEventListener('click', function() {
            const container = document.querySelector('.long-detail-container');
            container.classList.toggle('expanded');
            this.textContent = container.classList.contains('expanded') ? '상세정보 접기 ▲' : '상세정보 펼쳐보기 ▼';
            if (typeof recalculateOffsets === 'function') recalculateOffsets();
        });
    }

    // ==========================================================
    // 3. Sticky 네비게이션 & Scrollspy 기능 (기존과 동일)
    // ==========================================================
    const nav = document.querySelector('.product-detail-nav');
    let recalculateOffsets = function() {};
    if (nav) {
        // ... (이 부분은 기존 코드와 완전히 동일합니다) ...
        const navLinks = nav.querySelectorAll('a');
        const navItems = nav.querySelectorAll('li');
        const placeholder = document.querySelector('.nav-placeholder');
        let navOffsetTop = nav.offsetTop;
        const navHeight = nav.offsetHeight;
        let isAnimating = false;
        let sectionOffsets = [];
        placeholder.style.height = navHeight + 'px';
        recalculateOffsets = function() {
            sectionOffsets = [];
            navLinks.forEach(link => {
                const targetId = link.getAttribute('href');
                const target = document.querySelector(targetId);
                if (target) {
                    sectionOffsets.push({id: targetId, top: target.offsetTop});
                }
            });
            navOffsetTop = nav.classList.contains('sticky') ? navOffsetTop : nav.offsetTop;
        };
        function updateActiveNav(scrollTop) {
            let currentSectionId = null;
            const scrollPosition = scrollTop + navHeight + 20;
            sectionOffsets.forEach(section => {
                if (scrollPosition >= section.top) {
                    currentSectionId = section.id;
                }
            });
            navItems.forEach(item => item.classList.remove('active'));
            if (currentSectionId) {
                nav.querySelector(`a[href="${currentSectionId}"]`).parentElement.classList.add('active');
            }
        }
        recalculateOffsets();
        window.addEventListener('scroll', function() {
            const scrollTop = window.scrollY;
            if (scrollTop >= navOffsetTop) {
                nav.classList.add('sticky');
                placeholder.style.display = 'block';
            } else {
                nav.classList.remove('sticky');
                placeholder.style.display = 'none';
            }
            if (!isAnimating) {
                updateActiveNav(scrollTop);
            }
        });
        navLinks.forEach(link => {
            link.addEventListener('click', function(e) {
                e.preventDefault();
                const targetId = this.getAttribute('href');
                const target = document.querySelector(targetId);
                if (target) {
                    isAnimating = true;
                    const scrollToPosition = target.offsetTop - navHeight;
                    navItems.forEach(item => item.classList.remove('active'));
                    this.parentElement.classList.add('active');
                    window.scrollTo({top: scrollToPosition, behavior: 'smooth'});
                    setTimeout(() => { isAnimating = false; }, 800);
                }
            });
        });
    }

    // ==========================================================
    // 4. 리뷰 페이지네이션 (비동기) 기능
    // ==========================================================
    const pagination = document.getElementById('review-pagination');
    const reviewListContainer = document.getElementById('review-list-container');
    const detailReviewSection = document.getElementById('detail-review');

    if (pagination && reviewListContainer && detailReviewSection) {
        const prodNo = detailReviewSection.dataset.prodNo;
        const totalPages = parseInt(pagination.dataset.totalPages, 10); // 총 페이지 수

        // 리뷰를 불러오고 UI를 업데이트하는 함수
        function fetchAndUpdateReviews(page) {
            if (page < 1 || page > totalPages) {
                return; // 유효하지 않은 페이지 요청은 무시
            }

            fetch(`/shoply/api/reviews?prod_no=${prodNo}&page=${page}`)
                .then(response => response.json())
                .then(data => {
                    // 리뷰 목록 HTML 생성 및 업데이트
                    let newReviewsHtml = '';
                    data.forEach(review => {
                        newReviewsHtml += `
                            <div class="review-item">
                                <div class="review-meta-top">
                                    <div class="review-author">
                                        <div class="star-rating" data-score="${review.rev_rating}">
                                            <div class="star-rating-fill"><span>★</span><span>★</span><span>★</span><span>★</span><span>★</span></div>
                                            <div class="star-rating-base"><span>★</span><span>★</span><span>★</span><span>★</span><span>★</span></div>
                                        </div>
                                        <div class="rating-stars">(${review.rev_rating})</div>
                                        <div class="user-id">${review.privateMemId}</div>
                                    </div>
                                </div>
                                <div class="review-content">${review.rev_content}</div>
                                <div class="review-meta-bottom">
                                    <span class="date">${review.rev_rdate}</span>
                                </div>
                            </div>
                        `;
                    });
                    reviewListContainer.innerHTML = newReviewsHtml;

                    // 페이지네이션 UI 상태 업데이트
                    updatePaginationUI(page);

                    // 별점 UI 다시 렌더링
                    renderStarRatings();
                })
                .catch(error => console.error('리뷰를 불러오는 데 실패했습니다:', error));
        }

        // 페이지네이션 UI 상태를 업데이트하는 함수
        function updatePaginationUI(currentPage) {
            // 모든 페이지 링크에서 active 클래스 제거
            pagination.querySelectorAll('.page-link').forEach(link => link.classList.remove('active'));

            // 현재 페이지 번호에 active 클래스 추가
            const activeLink = pagination.querySelector(`.page-link[data-page="${currentPage}"]`);
            if (activeLink) activeLink.classList.add('active');

            // '이전', '다음' 버튼 활성화/비활성화 처리
            const prevButton = pagination.querySelector('.prev');
            const nextButton = pagination.querySelector('.next');
            if (prevButton) prevButton.classList.toggle('disabled', currentPage === 1);
            if (nextButton) nextButton.classList.toggle('disabled', currentPage === totalPages);
        }


        pagination.addEventListener('click', function(e) {
            e.preventDefault();
            const target = e.target;

            if (!target.classList.contains('page-link') || target.classList.contains('disabled')) {
                return;
            }

            const activeLink = pagination.querySelector('.page-link.active');
            const currentPage = activeLink ? parseInt(activeLink.dataset.page, 10) : 1;
            let targetPage;

            if (target.classList.contains('prev')) {
                targetPage = currentPage - 1;
            } else if (target.classList.contains('next')) {
                targetPage = currentPage + 1;
            } else {
                targetPage = parseInt(target.dataset.page, 10);
            }

            if (targetPage !== currentPage) {
                fetchAndUpdateReviews(targetPage);
            }
        });
    }
});