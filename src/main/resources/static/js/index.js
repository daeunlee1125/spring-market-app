const btn = document.getElementById("categoryBtn");
const box = document.getElementById("categoryBox");

// 버튼 클릭 → 토글
btn.addEventListener("click", (e) => {
  e.preventDefault();
  e.stopPropagation(); // 이벤트 전파 막기 (document로 전달 방지)

  const currentColor = getComputedStyle(btn).color;
  if (currentColor === "rgb(113, 69, 145)") {   
    btn.style.color = "black";
  } else {
    btn.style.color = "rgb(113, 69, 145)";
  }

  // 토글
  box.style.display = (box.style.display === "flex") ? "none" : "flex";
});

// 카테고리 박스 내부 클릭 → 닫히지 않도록
box.addEventListener("click", (e) => {
  e.stopPropagation();
});

// 문서 아무 곳이나 클릭 → 닫기
document.addEventListener("click", () => {
  if (box.style.display === "flex") {
    box.style.display = "none";
    btn.style.color = "black"; // 버튼 색도 초기화
  }
});

document.addEventListener("DOMContentLoaded", () => {
  const slides = document.querySelector(".slides");
  const slideImages = document.querySelectorAll(".slides img");
  const prevBtn = document.querySelector(".prev");
  const nextBtn = document.querySelector(".next");
  const dots = document.querySelectorAll(".indicators span");

  // 슬라이더 이미지가 없을 경우 오류 방지
  if (slides && slideImages.length > 0 && prevBtn && nextBtn && dots.length > 0) {
    let currentIndex = 1; // 클론 때문에 1부터 시작
    const totalSlides = slideImages.length;

    // 앞뒤 클론 추가
    const firstClone = slideImages[0].cloneNode(true);
    const lastClone = slideImages[totalSlides - 1].cloneNode(true);
    slides.appendChild(firstClone);
    slides.insertBefore(lastClone, slides.firstChild);

    const newSlides = document.querySelectorAll(".slides img");
    const slideCount = newSlides.length;

    slides.style.transform = `translateX(-${currentIndex * 100}%)`;

    // 슬라이드 이동 함수
    function goToSlide(index) {
      slides.style.transition = "transform 0.5s ease-in-out";
      slides.style.transform = `translateX(-${index * 100}%)`;
      currentIndex = index;

      // 인디케이터 갱신
      dots.forEach(dot => dot.classList.remove("active"));
      dots[(currentIndex - 1 + totalSlides) % totalSlides].classList.add("active");
    }

    // 다음 버튼
    nextBtn.addEventListener("click", () => {
      if (currentIndex >= slideCount - 1) return;
      goToSlide(currentIndex + 1);
    });

    // 이전 버튼
    prevBtn.addEventListener("click", () => {
      if (currentIndex <= 0) return;
      goToSlide(currentIndex - 1);
    });

    // transition 끝났을 때 위치 리셋
    slides.addEventListener("transitionend", () => {
      if (newSlides[currentIndex].isSameNode(firstClone)) {
        slides.style.transition = "none";
        currentIndex = 1;
        slides.style.transform = `translateX(-${currentIndex * 100}%)`;
      }
      if (newSlides[currentIndex].isSameNode(lastClone)) {
        slides.style.transition = "none";
        currentIndex = totalSlides;
        slides.style.transform = `translateX(-${currentIndex * 100}%)`;
      }
    });

    // 인디케이터 클릭
    dots.forEach((dot, idx) => {
      dot.addEventListener("click", () => {
        goToSlide(idx + 1); // 클론 때문에 +1
      });
    });

    // 초기화
    goToSlide(1);
  }


  const navLinks = document.querySelectorAll('.bottom-header .header-left a[href^="#"]');

  navLinks.forEach(link => {
      link.addEventListener('click', function(e) {
          // a 태그의 기본 동작(페이지 점프)을 막음
          e.preventDefault();

          // 클릭한 a 태그의 href 속성값을 가져옴 (예: "#hit-products")
          const targetId = this.getAttribute('href');
          
          // href 값에 해당하는 id를 가진 요소를 찾음
          const targetSection = document.querySelector(targetId);

          // 해당 요소가 페이지에 존재하면
          if (targetSection) {
              // 해당 섹션의 위치로 부드럽게 스크롤
              targetSection.scrollIntoView({
                  behavior: 'smooth',
                  block: 'start'
              });
          }
      });
  });

});

// main menu hover 시 sub menu 교체
const mainItems = document.querySelectorAll(".main-menu li");
const subContents = document.querySelectorAll(".submenu-content");

mainItems.forEach(item => {
  item.addEventListener("mouseenter", () => {
    const target = item.getAttribute("data-target");
    subContents.forEach(c => {
      c.style.display = (c.id === target) ? "block" : "none";
    });
    mainItems.forEach(i => {
      i.style.background = "rgb(236, 226, 236)";
      i.style.color = "black";
      i.style.fontWeight = "400";
      
    });
    item.style.background = "#3a303aff";
    item.style.color = "white";
    item.style.fontWeight = "bold";
  });
});