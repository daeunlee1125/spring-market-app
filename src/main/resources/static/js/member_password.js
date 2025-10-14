//member_password

document.addEventListener('DOMContentLoaded', function() {

    // 탭 전환 기능
    const tabButtons = document.querySelectorAll('.tab-button');
    const tabContents = document.querySelectorAll('.tab-content');

    tabButtons.forEach(button => {
        button.addEventListener('click', function() {
            const targetTab = this.getAttribute('data-tab');

            // 모든 탭 버튼 비활성화
            tabButtons.forEach(btn => btn.classList.remove('active'));
            // 현재 클릭된 탭 버튼 활성화
            this.classList.add('active');

            // 모든 탭 콘텐츠 숨기기
            tabContents.forEach(content => content.classList.remove('active'));
            // 해당 탭 콘텐츠 보이기
            document.getElementById(targetTab + '-tab').classList.add('active');
        });
    });

    //인증기능 시작
    const btnVerify = document.querySelector('.btn-verify');
    const btnOk = document.querySelector('.btn-check');
    const btnSubmit = document.querySelector('.btn-submit');
    const btnCancel = document.querySelector('.btn-cancel');
    const emailForm = document.querySelector('form[name="emailForm"]');

    let isEmailVerified = false; // 인증 완료 여부
    let canResend = true;

    // 인증번호 받기 버튼 클릭
    btnVerify.addEventListener('click', async function(e) {
        e.preventDefault();

        if(!canResend) {
            alert('잠시 후 다시 시도해주세요.');
            return;
        }

        const memId = emailForm.mem_id.value.trim();
        const email = emailForm.mem_email.value.trim();

        if(!memId) {
            alert('아이디를 입력해주세요.');
            return;
        }

        if(!email) {
            alert('이메일을 입력해주세요.');
            return;
        }

        const jsonData = {
            "id": memId,
            "email": email
        };

        // 버튼 비활성화 및 텍스트 변경
        btnVerify.disabled = true;
        btnVerify.textContent = '전송중...';

        try {
            const response = await fetch('/shoply/member/find/emailPass', {
                method: 'POST',
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify(jsonData)
            });

            if (!response.ok) {
                throw new Error('서버 응답 오류');
            }

            const data = await response.json();

            if(data.count > 0) {
                alert('인증번호가 이메일로 발송되었습니다.');
                emailForm.auth.disabled = false;
                btnOk.disabled = false;

                // 60초 타이머 시작
                canResend = false;
                let countdown = 60;

                const timer = setInterval(() => {
                    btnVerify.textContent = `재전송 (${countdown}초)`;
                    countdown--;

                    if(countdown < 0) {
                        clearInterval(timer);
                        btnVerify.disabled = false;
                        btnVerify.textContent = '재전송';
                        canResend = true;
                    }
                }, 1000);

            } else {
                btnVerify.disabled = false;
                btnVerify.textContent = '인증번호 받기';
                alert('입력하신 아이디와 이메일이 일치하는 회원정보가 없습니다.');
            }
        } catch(error) {
            console.error('Error:', error);
            btnVerify.disabled = false;
            btnVerify.textContent = '인증번호 받기';
            alert('인증번호 발송 중 오류가 발생했습니다.');
        }
    });

    // 인증번호 확인 버튼 클릭
    btnOk.addEventListener('click', async function(e) {
        e.preventDefault();

        const code = emailForm.auth.value.trim();

        if(!code) {
            alert('인증번호를 입력해주세요.');
            return;
        }

        const jsonData = {
            "code": code
        };

        try {
            const response = await fetch('/shoply/member/find/verifyCode', {
                method: 'POST',
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify(jsonData)
            });

            const data = await response.json();

            if(data.valid) {
                alert('인증이 완료되었습니다.');
                isEmailVerified = true;

                // 확인 버튼 비활성화
                btnOk.disabled = true;
                btnOk.textContent = '인증완료';
            } else {
                alert('인증번호가 일치하지 않습니다. 다시 확인해주세요.');
            }
        } catch(error) {
            console.error('Error:', error);
            alert('인증번호 확인 중 오류가 발생했습니다.');
        }
    });

    // 다음 버튼 클릭
    btnSubmit.addEventListener('click', function(e) {
        e.preventDefault();

        if(!isEmailVerified) {
            alert('이메일 인증을 완료해주세요.');
            return;
        }

        const memId = document.getElementById('mem_id').value;

        /*emailForm.action = `/shoply/member/find/changePassword?mem_id=${memId}`;
        emailForm.submit();*/

        location.href = `/shoply/member/find/changePassword?mem_id=${memId}`;
    });

    // 취소 버튼
    btnCancel.addEventListener('click', function() {
        location.href = '/shoply/member/login'; // 로그인 페이지로 이동
    });

});
