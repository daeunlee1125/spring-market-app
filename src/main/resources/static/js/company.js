const slider = document.querySelector('.slider');
const slides = document.querySelectorAll('.slide');
const prevBtn = document.querySelector('.prev');
const nextBtn = document.querySelector('.next');

let currentIndex = 1; // 복제 때문에 시작은 1번 슬라이드
const totalSlides = slides.length;

// 👉 앞뒤 슬라이드 복제
const firstClone = slides[0].cloneNode(true);
const lastClone = slides[totalSlides - 1].cloneNode(true);

firstClone.id = "first-clone";
lastClone.id = "last-clone";

slider.appendChild(firstClone);
slider.insertBefore(lastClone, slides[0]);

const allSlides = document.querySelectorAll('.slide');
let slideWidth = 100; // % 기준
slider.style.transform = `translateX(-${currentIndex * slideWidth}%)`;

// 슬라이드 이동 함수
function showSlide(index) {
  slider.style.transition = "transform 0.5s ease-in-out";
  slider.style.transform = `translateX(-${index * slideWidth}%)`;
  currentIndex = index;
}

// 트랜지션 끝났을 때 위치 보정
slider.addEventListener("transitionend", () => {
  if (allSlides[currentIndex].id === "first-clone") {
    slider.style.transition = "none";
    currentIndex = 1; // 진짜 첫 번째
    slider.style.transform = `translateX(-${currentIndex * slideWidth}%)`;
  }
  if (allSlides[currentIndex].id === "last-clone") {
    slider.style.transition = "none";
    currentIndex = totalSlides; // 진짜 마지막
    slider.style.transform = `translateX(-${currentIndex * slideWidth}%)`;
  }
});

// 버튼 클릭
prevBtn.addEventListener("click", () => {
  if (currentIndex <= 0) return;
  showSlide(currentIndex - 1);
});

nextBtn.addEventListener("click", () => {
  if (currentIndex >= allSlides.length - 1) return;
  showSlide(currentIndex + 1);
});




