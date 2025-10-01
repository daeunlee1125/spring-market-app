$(function(){
// 드롭다운 버튼 클릭 시 옵션 목록 토글
$('.sort-selected').on('click', function(e) {
    e.stopPropagation(); // 이벤트 버블링 방지
    $('.sort-options').toggle();
    $('.sort-dropdown').toggleClass('active');
});

// 옵션 항목 클릭 시
$('.sort-options li a').on('click', function() {
    // 선택된 항목의 텍스트를 버튼에 표시 (실제 페이지 이동을 막지는 않음)
    var selectedText = $(this).text();
    $('#selected-sort-text').text(selectedText);

    // 옵션 목록 숨기기
    $('.sort-options').hide();
    $('.sort-dropdown').removeClass('active');
});

// 문서의 다른 곳을 클릭하면 드롭다운 닫기
$(document).on('click', function() {
    $('.sort-options').hide();
    $('.sort-dropdown').removeClass('active');
});
});

$(document).ready(function() {
// --- 설정 변수 ---
const itemsPerPage = 4; // 한 페이지에 보여줄 상품 수

// --- DOM 요소 선택 ---
const $productList = $('.product-list-container');
const $items = $productList.find('.product-item');
const $pagination = $('.pagination');

// --- 초기화 ---
const totalItems = $items.length;
if (totalItems === 0) return; // 상품이 없으면 실행 중단

const totalPages = Math.ceil(totalItems / itemsPerPage);
let currentPage = 1;

// 페이지 번호 생성 함수
function setupPagination() {
    $pagination.empty(); // 기존 페이지 번호 삭제

    // '이전' 버튼 추가
    $pagination.append('<a href="#" class="page-link prev">이전</a>');

    // 페이지 번호 링크 추가
    for (let i = 1; i <= totalPages; i++) {
    $pagination.append(`<a href="#" class="page-link" data-page="${i}">${i}</a>`);
    }

    // '다음' 버튼 추가
    $pagination.append('<a href="#" class="page-link next">다음</a>');
}

// 특정 페이지를 보여주는 함수
function showPage(page) {
    currentPage = page;
    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;

    // 모든 상품 숨기기
    $items.hide();
    // 현재 페이지의 상품만 보여주기
    $items.slice(startIndex, endIndex).show();

    // 페이지네이션 링크 상태 업데이트
    updatePaginationLinks();
}

// 페이지네이션 링크 상태(active, disabled) 업데이트 함수
function updatePaginationLinks() {
    // 'active' 클래스 관리
    $pagination.find('.page-link').removeClass('active');
    $pagination.find(`.page-link[data-page="${currentPage}"]`).addClass('active');

    // 'disabled' 클래스 관리
    $pagination.find('.prev').toggleClass('disabled', currentPage === 1);
    $pagination.find('.next').toggleClass('disabled', currentPage === totalPages);
}

// 페이지네이션 클릭 이벤트
$pagination.on('click', '.page-link', function(e) {
    e.preventDefault();
    const $this = $(this);

    if ($this.hasClass('disabled') || $this.hasClass('active')) {
        return; // 비활성화 또는 현재 페이지 버튼이면 아무것도 안함
    }

    let targetPage;
    if ($this.hasClass('prev')) {
        targetPage = currentPage - 1;
    } else if ($this.hasClass('next')) {
        targetPage = currentPage + 1;
    } else {
        targetPage = parseInt($this.data('page'));
    }

    if (targetPage >= 1 && targetPage <= totalPages) {
        showPage(targetPage);
    }
});

// --- 최초 실행 ---
if (totalPages > 1) {
    setupPagination(); // 페이지가 2개 이상일 때만 페이지네이션 생성
}
showPage(1); // 첫 번째 페이지를 기본으로 보여줌
});