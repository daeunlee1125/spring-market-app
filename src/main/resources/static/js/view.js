document.addEventListener('DOMContentLoaded', function() {

    // 별점 기능 (Vanilla JS)
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

    // ==========================================================
    // 1. 옵션 선택 및 가격 계산 기능 (Vanilla JS)
    // ==========================================================
    const productInfoSection = document.querySelector('.product-info-section');
    const basePrice = parseInt(productInfoSection.dataset.productPrice);

    if (!isNaN(basePrice)) {
        const selectColor = document.querySelector('#select-color');
        const selectSize = document.querySelector('#select-size');
        const selectedOptionsList = document.querySelector('#selected-options-list');
        const grandTotalPriceEl = document.querySelector('#grand-total-price');

        function updateGrandTotal() {
            let grandTotal = 0;
            const selectedItems = document.querySelectorAll('.selected-item');
            selectedItems.forEach(item => {
                const quantityInput = item.querySelector('.quantity-input');
                const quantity = parseInt(quantityInput.value);
                const itemTotal = basePrice * quantity;

                const itemTotalPriceEl = item.querySelector('.item-total-price');
                itemTotalPriceEl.textContent = itemTotal.toLocaleString() + '원';

                grandTotal += itemTotal;
            });
            grandTotalPriceEl.textContent = grandTotal.toLocaleString() + '원';
        }

        function handleOptionChange() {
            const color = selectColor.value;
            const size = selectSize.value;

            if (color && size) {
                const optionText = `베이직 레더 벨트 / ${color} / ${size}`;

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
                        <div class="selected-item">
                            <span class="item-name">${optionText}</span>
                            <div class="quantity-counter">
                                <button class="btn-minus">-</button>
                                <input type="text" class="quantity-input" value="1" readonly>
                                <button class="btn-plus">+</button>
                            </div>
                            <span class="item-total-price">${basePrice.toLocaleString()}원</span>
                            <button class="btn-remove">×</button>
                        </div>`;
                    selectedOptionsList.insertAdjacentHTML('beforeend', newItemHtml);
                }

                selectColor.value = '';
                selectSize.value = '';
                updateGrandTotal();
            }
        }

        selectColor.addEventListener('change', handleOptionChange);
        selectSize.addEventListener('change', handleOptionChange);

        selectedOptionsList.addEventListener('click', function(event) {
            const target = event.target;
            if (target.tagName === 'BUTTON') {
                const item = target.closest('.selected-item');
                if (!item) return;

                const quantityInput = item.querySelector('.quantity-input');
                let quantity = parseInt(quantityInput.value);

                if (target.classList.contains('btn-plus')) {
                    quantity++;
                } else if (target.classList.contains('btn-minus')) {
                    if (quantity > 1) {
                        quantity--;
                    }
                } else if (target.classList.contains('btn-remove')) {
                    item.remove();
                }

                if (quantityInput) {
                    quantityInput.value = quantity;
                }
                updateGrandTotal();
            }
        });
    }

    // ==========================================================
    // 2. 상세정보 펼치기/접기 기능 (Vanilla JS)
    // ==========================================================
    const toggleDetailBtn = document.querySelector('.toggle-detail-btn');
    if (toggleDetailBtn) {
        toggleDetailBtn.addEventListener('click', function() {
            const container = document.querySelector('.long-detail-container');
            container.classList.toggle('expanded');

            if (container.classList.contains('expanded')) {
                this.textContent = '상세정보 접기 ▲';
            } else {
                this.textContent = '상세정보 펼쳐보기 ▼';
            }
            recalculateOffsets();
        });
    }

    // ==========================================================
    // 3. Sticky 네비게이션 & Scrollspy 기능 (Vanilla JS)
    // ==========================================================
    const nav = document.querySelector('.product-detail-nav');
    let recalculateOffsets = function() {}; // 함수 선언

    if (nav) {
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
                    sectionOffsets.push({
                        id: targetId,
                        top: target.offsetTop
                    });
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

                    window.scrollTo({
                        top: scrollToPosition,
                        behavior: 'smooth'
                    });

                    // 'smooth' 스크롤이 끝나는 것을 감지하는 정확한 이벤트가 없으므로
                    // 일정 시간 후 플래그를 해제합니다.
                    setTimeout(() => {
                        isAnimating = false;
                    }, 800); // 애니메이션 시간보다 넉넉하게 설정
                }
            });
        });
    }

    // ==========================================================
    // 4. 리뷰 페이지네이션 기능 (Vanilla JS)
    // ==========================================================
    const reviewContainer = document.querySelector('#detail-review');
    if (reviewContainer) {
        const reviewItems = Array.from(reviewContainer.querySelectorAll('.review-item'));

        if (reviewItems.length > 0) {
            const reviewsPerPage = 5;
            const totalReviews = reviewItems.length;
            const totalPages = Math.ceil(totalReviews / reviewsPerPage);
            let currentReviewPage = 1;

            const reviewPagination = document.querySelector('.pagination-container .pagination');

            function showReviewPage(page) {
                currentReviewPage = page;
                const startIndex = (currentReviewPage - 1) * reviewsPerPage;
                const endIndex = startIndex + reviewsPerPage;

                reviewItems.forEach(item => item.style.display = 'none');
                reviewItems.slice(startIndex, endIndex).forEach(item => item.style.display = 'block');

                updateReviewPaginationLinks();
            }

            function updateReviewPaginationLinks() {
                const pageLinks = reviewPagination.querySelectorAll('.page-link');
                pageLinks.forEach(link => link.classList.remove('active'));

                const activeLink = reviewPagination.querySelector(`.page-link[data-page="${currentReviewPage}"]`);
                if(activeLink) activeLink.classList.add('active');

                reviewPagination.querySelector('.prev').classList.toggle('disabled', currentReviewPage === 1);
                reviewPagination.querySelector('.next').classList.toggle('disabled', currentReviewPage === totalPages);
            }

            reviewPagination.addEventListener('click', function(e) {
                e.preventDefault();
                const target = e.target;

                if (!target.classList.contains('page-link') || target.classList.contains('disabled') || target.classList.contains('active')) {
                    return;
                }

                let targetPage;
                if (target.classList.contains('prev')) {
                    targetPage = currentReviewPage - 1;
                } else if (target.classList.contains('next')) {
                    targetPage = currentReviewPage + 1;
                } else {
                    targetPage = parseInt(target.dataset.page);
                }

                if (targetPage >= 1 && targetPage <= totalPages) {
                    showReviewPage(targetPage);
                }
            });

            showReviewPage(1);
        }
    }
});