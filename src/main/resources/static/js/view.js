$(document).ready(function() {
    
    // ==========================================================
    // 1. 옵션 선택 및 가격 계산 기능
    // ==========================================================
    const basePrice = parseInt($('.product-info-section').data('product-price'));

    // basePrice 값이 유효한 숫자인지 확인
    if (!isNaN(basePrice)) {
        // 색상 또는 사이즈 선택 시 이벤트 처리
        $('#select-color, #select-size').on('change', function() {
            const color = $('#select-color').val();
            const size = $('#select-size').val();

            if (color && size) {
                const optionText = `베이직 레더 벨트 / ${color} / ${size}`;
                
                let isDuplicate = false;
                $('.selected-item').each(function() {
                    if ($(this).find('.item-name').text() === optionText) {
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
                    
                    $('#selected-options-list').append(newItemHtml);
                }

                $('#select-color').val('');
                $('#select-size').val('');
                
                updateGrandTotal();
            }
        });

        // 수량 변경 및 삭제 버튼 이벤트 (이벤트 위임)
        $('#selected-options-list').on('click', 'button', function() {
            const $item = $(this).closest('.selected-item');
            const $quantityInput = $item.find('.quantity-input');
            let quantity = parseInt($quantityInput.val());

            if ($(this).hasClass('btn-plus')) {
                quantity++;
            } else if ($(this).hasClass('btn-minus')) {
                if (quantity > 1) {
                    quantity--;
                }
            } else if ($(this).hasClass('btn-remove')) {
                $item.remove();
            }

            if ($quantityInput.length > 0) {
                $quantityInput.val(quantity);
            }
            updateGrandTotal();
        });

        // 총 금액 계산 및 업데이트 함수
        function updateGrandTotal() {
            let grandTotal = 0;
            $('.selected-item').each(function() {
                const $item = $(this);
                const quantity = parseInt($item.find('.quantity-input').val());
                const itemTotal = basePrice * quantity;
                
                $item.find('.item-total-price').text(itemTotal.toLocaleString() + '원');
                
                grandTotal += itemTotal;
            });
            
            $('#grand-total-price').text(grandTotal.toLocaleString() + '원');
        }
    }

    // ==========================================================
    // 2. 상세정보 펼치기/접기 기능
    // ==========================================================
    $('.toggle-detail-btn').on('click', function() {
        const $container = $('.long-detail-container');
        const $button = $(this);

        $container.toggleClass('expanded');

        if ($container.hasClass('expanded')) {
            $button.text('상세정보 접기 ▲');
        } else {
            $button.text('상세정보 펼쳐보기 ▼');
        }
        
        recalculateOffsets();
    });

    // ==========================================================
    // 3. Sticky 네비게이션 & Scrollspy 기능
    // ==========================================================
    const $nav = $('.product-detail-nav');
    let recalculateOffsets = function() {}; // 함수 선언

    if ($nav.length) {
        
        const $navLinks = $nav.find('a');
        const $navItems = $nav.find('li');
        const $placeholder = $('.nav-placeholder');
        let navOffsetTop = $nav.offset().top;
        const navHeight = $nav.outerHeight();
        
        let isAnimating = false;
        let sectionOffsets = [];

        $placeholder.height(navHeight);

        recalculateOffsets = function() {
            sectionOffsets = [];
            $navLinks.each(function() {
                const targetId = $(this).attr('href');
                const $target = $(targetId);
                if ($target.length) {
                    sectionOffsets.push({
                        id: targetId,
                        top: $target.offset().top
                    });
                }
            });
            navOffsetTop = $nav.is('.sticky') ? navOffsetTop : $nav.offset().top;
        };

        recalculateOffsets();

        $(window).on('scroll', function() {
            const scrollTop = $(this).scrollTop();

            if (scrollTop >= navOffsetTop) {
                $nav.addClass('sticky');
                $placeholder.show();
            } else {
                $nav.removeClass('sticky');
                $placeholder.hide();
            }
            
            if (!isAnimating) {
                updateActiveNav(scrollTop);
            }
        });

        function updateActiveNav(scrollTop) {
            let currentSectionId = null;
            const scrollPosition = scrollTop + navHeight + 20;

            for (const section of sectionOffsets) {
                if (scrollPosition >= section.top) {
                    currentSectionId = section.id;
                }
            }
            
            $navItems.removeClass('active');
            if (currentSectionId) {
                $nav.find(`a[href="${currentSectionId}"]`).parent('li').addClass('active');
            }
        }

        $navLinks.on('click', function(e) {
            e.preventDefault();
            
            const targetId = $(this).attr('href');
            const $target = $(targetId);

            if ($target.length) {
                isAnimating = true;
                
                const scrollToPosition = $target.offset().top - navHeight;
                
                $navItems.removeClass('active');
                $(this).parent('li').addClass('active');

                $('html, body').stop().animate({
                    scrollTop: scrollToPosition
                }, 500, function() {
                    isAnimating = false;
                });
            }
        });
    }

    // ==========================================================
    // 4. 리뷰 페이지네이션 기능 (수정된 코드)
    // ==========================================================
    const $reviewContainer = $('#detail-review');
    const $reviewItems = $reviewContainer.find('.review-item');

    if ($reviewItems.length > 0) {
        const reviewsPerPage = 5; // 한 페이지에 보여줄 리뷰 수
        const totalReviews = $reviewItems.length;
        const totalPages = Math.ceil(totalReviews / reviewsPerPage);
        let currentReviewPage = 1;

        const $reviewPagination = $('.pagination-container .pagination');

        function setupReviewPagination() {
            $reviewPagination.empty();
            $reviewPagination.append('<a href="#" class="page-link prev">이전</a>');
            for (let i = 1; i <= totalPages; i++) {
                $reviewPagination.append(`<a href="#" class="page-link" data-page="${i}">${i}</a>`);
            }
            $reviewPagination.append('<a href="#" class="page-link next">다음</a>');
        }

        function showReviewPage(page) {
            currentReviewPage = page;
            const startIndex = (currentReviewPage - 1) * reviewsPerPage;
            const endIndex = startIndex + reviewsPerPage;

            $reviewItems.hide();
            $reviewItems.slice(startIndex, endIndex).show();

            updateReviewPaginationLinks();
        }

        function updateReviewPaginationLinks() {
            $reviewPagination.find('.page-link').removeClass('active');
            $reviewPagination.find(`.page-link[data-page="${currentReviewPage}"]`).addClass('active');
            $reviewPagination.find('.prev').toggleClass('disabled', currentReviewPage === 1);
            $reviewPagination.find('.next').toggleClass('disabled', currentReviewPage === totalPages);
        }

        // 페이지네이션 클릭 이벤트 (list 페이지와 동일한 로직)
        $reviewPagination.on('click', '.page-link', function(e) {
            e.preventDefault();
            const $this = $(this);

            if ($this.hasClass('disabled') || $this.hasClass('active')) {
                return;
            }

            let targetPage;
            if ($this.hasClass('prev')) {
                targetPage = currentReviewPage - 1;
            } else if ($this.hasClass('next')) {
                targetPage = currentReviewPage + 1;
            } else {
                targetPage = parseInt($this.data('page'));
            }

            if (targetPage >= 1 && targetPage <= totalPages) {
                showReviewPage(targetPage);
            }
        });

        // --- 리뷰 페이지네이션 최초 실행 ---
        if (totalPages > 1) {
             // HTML에 페이지네이션이 하드코딩 되어있으므로 이 부분은 필요 시 주석 해제
            // setupReviewPagination();
        }
        showReviewPage(1); // 첫 페이지를 기본으로 보여줌
    }
});