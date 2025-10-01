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

    alert('약관 동의가 완료되었습니다.');
    // 여기에서 다음 페이지로 이동하거나 추가 처리를 할 수 있습니다.
    return true;
}