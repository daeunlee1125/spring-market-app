// hero 글자 appear 효과
window.addEventListener('DOMContentLoaded', () => {
  const title = document.querySelector('.hero-title');
  const subtitle = document.querySelector('.hero-subtitle');

  // 페이지 로드 후 active 클래스 추가
  setTimeout(() => {
    title.classList.add('active');
  }, 200); // 0.2초 뒤 실행

  setTimeout(() => {
    subtitle.classList.add('active');
  }, 600); // 0.6초 뒤 실행
});
