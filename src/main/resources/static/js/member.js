
//signup.html 동의하기 버튼 클릭시 작동
function checkAgreement() {
    const requiredTerms = ['terms1', 'terms2', 'terms3'];
    const uncheckedRequired = [];

    for (let termId of requiredTerms) {
        if (!document.getElementById(termId).checked) {
            uncheckedRequired.push(termId);
        }
    }

    if (uncheckedRequired.length > 0) {
        alert('필수 약관에 모두 동의해주세요.');
        return false;
    }

    // 버튼의 data-type 속성에서 type 가져오기
    const type = document.getElementById('submitBtn').dataset.type || 'general';
    window.location.href = (type === 'seller') ? '/shoply/member/registerSeller' : '/shoply/member/register';

    return true;
}



// 소셜 로그인 함수들
function loginWithNaver() {
    console.log('네이버 로그인 API 호출');
    alert('네이버 로그인 API를 연동해주세요.');
}

function loginWithKakao() {
    console.log('카카오 로그인 API 호출');
    alert('카카오 로그인 API를 연동해주세요.');
}

function loginWithGoogle() {
    console.log('구글 로그인 API 호출');
    alert('구글 로그인 API를 연동해주세요.');
}

// 로그인 폼 제출
document.querySelector('.login-btn').addEventListener('click', function(e) {
    e.preventDefault();
    const userId = document.getElementById('userId').value;
    const password = document.getElementById('password').value;

    if (!userId || !password) {
        alert('아이디와 비밀번호를 입력해주세요.');
        return;
    }

    alert('로그인 기능은 구현되지 않았습니다.');
});