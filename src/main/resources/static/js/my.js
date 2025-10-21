// my.js -
// ============================
// 모달 공통 제어
// ============================

function openModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) modal.style.display = 'flex';
}

function closeModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) modal.style.display = 'none';
}

// 모달 외부 클릭 시 닫기
window.onclick = function(event) {
    if (event.target.classList.contains('modal')) {
        event.target.style.display = 'none';
    }
}

// ============================
// DOMContentLoaded 이후 실행
// ============================
document.addEventListener("DOMContentLoaded", function() {

    // ----------------------------
    // 1️⃣ 리뷰 작성 폼
    // ----------------------------
    const reviewForm = document.querySelector("#reviewForm");
    if (reviewForm) {
        reviewForm.addEventListener("submit", function(e) {
            e.preventDefault();

            const prodNo = document.getElementById('review_prod_no').value;
            const content = document.getElementById('review_content').value.trim();
            const rating = document.querySelector('input[name="rating"]:checked');

            // 유효성 검사
            if (!prodNo) {
                alert('상품 정보를 찾을 수 없습니다.');
                return;
            }
            if (!rating) {
                alert('별점을 선택해주세요.');
                return;
            }
            if (content.length < 10) {
                alert('리뷰 내용을 최소 10자 이상 입력해주세요.');
                return;
            }

            const formData = new FormData(reviewForm);

            fetch("/shoply/my/review/write", { method: "POST", body: formData })
                .then(res => res.json())
                .then(data => {
                    if (data.success) {
                        alert("리뷰 작성 완료!");
                        closeModal('reviewModal');
                        reviewForm.reset();
                        location.reload(); // 페이지 새로고침
                    } else {
                        alert("리뷰 작성 실패: " + data.message);
                    }
                })
                .catch(err => {
                    console.error("Ajax Error:", err);
                    alert("리뷰 작성 중 오류가 발생했습니다.");
                });
        });
    }

    // ----------------------------
    // 2️⃣ 문의하기 폼
    // ----------------------------
    const qnaForm = document.querySelector("#qnaForm");
    if (qnaForm) {
        qnaForm.addEventListener("submit", function(e) {
            e.preventDefault();

            const qnaType = document.querySelector('input[name="qna_type"]:checked');
            const title = qnaForm.querySelector('input[name="title"]').value.trim();
            const content = qnaForm.querySelector('textarea[name="content"]').value.trim();

            if (!qnaType) {
                alert('문의유형을 선택해주세요.');
                return;
            }
            if (!title) {
                alert('제목을 입력해주세요.');
                return;
            }
            if (!content) {
                alert('내용을 입력해주세요.');
                return;
            }

            const formData = new FormData(qnaForm);

            fetch("/shoply/my/qna/write", { method: "POST", body: formData })
                .then(res => res.text())
                .then(data => {
                    if (data === 'success') {
                        alert("문의 등록 완료!");
                        closeModal('qnaPopup');
                        qnaForm.reset();
                        location.reload();
                    } else {
                        alert("문의 등록 실패");
                    }
                })
                .catch(err => {
                    console.error("Ajax Error:", err);
                    alert("문의 등록 중 오류가 발생했습니다.");
                });
        });
    }

    // ----------------------------
    // 4️⃣ 반품/교환 신청 폼
    // ----------------------------
    const returnForm = document.getElementById('returnForm');
    if (returnForm) {
        returnForm.addEventListener('submit', function(e) {
            e.preventDefault();

            const modal = document.getElementById('returnModal');
            const itemNo = modal.dataset.itemNo;
            const reason = returnForm.querySelector('select[name="reason"]').value;
            const detail = returnForm.querySelector('textarea[name="detail"]').value.trim();

            if (!reason) { alert('반품 사유를 선택해주세요.'); return; }
            if (!detail) { alert('상세 내용을 입력해주세요.'); return; }

            const fullReason = reason + (detail ? ' - ' + detail : '');

            fetch(`/shoply/my/order/return?item_no=${itemNo}&reason=${encodeURIComponent(fullReason)}`, {
                method: 'POST'
            })
                .then(res => res.text())
                .then(data => {
                    if (data === 'success') {
                        alert('반품 신청이 완료되었습니다.');
                        closeModal('returnModal');
                        returnForm.reset();
                        location.reload();
                    } else {
                        alert('반품 신청 실패: ' + data);
                    }
                })
                .catch(err => {
                    console.error(err);
                    alert('오류가 발생했습니다.');
                });
        });
    }

    const exchangeForm = document.getElementById('exchangeForm');
    if (exchangeForm) {
        exchangeForm.addEventListener('submit', function(e) {
            e.preventDefault();

            const modal = document.getElementById('exchangeModal');
            const itemNo = modal.dataset.itemNo;
            const reason = exchangeForm.querySelector('select[name="reason"]').value;
            const detail = exchangeForm.querySelector('textarea[name="detail"]').value.trim();

            if (!reason) { alert('교환 사유를 선택해주세요.'); return; }
            if (!detail) { alert('상세 내용을 입력해주세요.'); return; }

            const fullReason = reason + (detail ? ' - ' + detail : '');

            fetch(`/shoply/my/order/exchange?item_no=${itemNo}&reason=${encodeURIComponent(fullReason)}`, {
                method: 'POST'
            })
                .then(res => res.text())
                .then(data => {
                    if (data === 'success') {
                        alert('교환 신청이 완료되었습니다.');
                        closeModal('exchangeModal');
                        exchangeForm.reset();
                        location.reload();
                    } else {
                        alert('교환 신청 실패: ' + data);
                    }
                })
                .catch(err => {
                    console.error(err);
                    alert('오류가 발생했습니다.');
                });
        });
    }

    // ----------------------------
    // 5️⃣ 비밀번호 변경 폼
    // ----------------------------
    const passwordForm = document.getElementById('passwordForm');
    if (passwordForm) {
        passwordForm.addEventListener('submit', function(e) {
            e.preventDefault();

            const currentPassword = document.getElementById('currentPassword').value.trim();
            const newPassword = document.getElementById('newPassword').value.trim();
            const confirmPassword = document.getElementById('confirmPassword').value.trim();

            if (!currentPassword) {
                alert('현재 비밀번호를 입력해주세요.');
                return;
            }

            if (!newPassword) {
                alert('새 비밀번호를 입력해주세요.');
                return;
            }

            if (newPassword.length < 6) {
                alert('비밀번호는 최소 6자 이상이어야 합니다.');
                return;
            }

            if (newPassword !== confirmPassword) {
                alert('비밀번호가 일치하지 않습니다.');
                return;
            }

            fetch('/shoply/my/info/changePassword', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    currentPassword: currentPassword,
                    newPassword: newPassword
                })
            })
                .then(res => res.text())
                .then(data => {
                    if (data === 'success') {
                        alert('비밀번호가 변경되었습니다.');
                        closeModal('passwordModal');
                        passwordForm.reset();
                    } else {
                        alert('비밀번호 변경 실패: ' + data);
                    }
                })
                .catch(err => {
                    console.error(err);
                    alert('오류가 발생했습니다.');
                });
        });
    }

});

// =======================
// 이메일 도메인 선택
// =======================
function checkEmailDomain() {
    const domainSelect = document.getElementById('emailDomain');
    const emailInput = document.getElementById('emailInput');
    if (emailInput) {
        emailInput.style.display = domainSelect.value === 'self' ? 'inline-block' : 'none';
    }
}

// =======================
// 주소 검색 (Daum Postcode API)
// =======================
function searchAddress() {
    new daum.Postcode({
        oncomplete: function(data) {
            document.getElementById('addr1').value = data.address;
            document.getElementById('zipcode').value = data.zonecode;
        }
    }).open();
}

// =======================
// 회원 탈퇴
// =======================
function confirmWithdrawal() {
    if (!confirm('정말 탈퇴하시겠습니까?')) return;

    fetch('/shoply/my/info/withdrawal', { method: 'POST' })
        .then(res => {
            if (res.ok) {
                alert('회원 탈퇴가 완료되었습니다.');
                window.location.href = '/';
            } else {
                alert('탈퇴 실패');
            }
        })
        .catch(err => {
            console.error(err);
            alert('오류가 발생했습니다.');
        });
}

// =======================
// 회원 정보 수정
// =======================
function saveChanges() {
    const emailId = document.getElementById('emailId').value.trim();
    const domain = document.getElementById('emailDomain').value === 'self'
        ? document.getElementById('emailInput').value.trim()
        : document.getElementById('emailDomain').value;

    if (!emailId || !domain) {
        alert('이메일을 올바르게 입력해주세요.');
        return;
    }

    const hp1 = document.getElementById('hp1').value.trim();
    const hp2 = document.getElementById('hp2').value.trim();
    const hp3 = document.getElementById('hp3').value.trim();

    if (!hp1 || !hp2 || !hp3) {
        alert('휴대폰 번호를 올바르게 입력해주세요.');
        return;
    }

    const memberData = {
        mem_email: `${emailId}@${domain}`,
        mem_hp: `${hp1}-${hp2}-${hp3}`,
        mem_zip: document.getElementById('zipcode').value,
        mem_addr1: document.getElementById('addr1').value,
        mem_addr2: document.getElementById('addr2').value
    };

    fetch('/shoply/my/info/update', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(memberData)
    })
        .then(res => res.text())
        .then(data => {
            if (data === 'success') {
                alert('회원 정보가 수정되었습니다.');
                location.reload();
            } else {
                alert('수정 실패');
            }
        })
        .catch(err => {
            console.error(err);
            alert('오류가 발생했습니다.');
        });
}
