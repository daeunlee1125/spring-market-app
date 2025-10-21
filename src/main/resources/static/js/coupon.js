document.addEventListener("DOMContentLoaded", () => {
    const btn = document.querySelector(".btn-coupon-claim");
    if (!btn) return;

    btn.addEventListener("click", async () => {
        const sellerId = btn.dataset.seller; // 판매자 ID 전송

        const response = await fetch("/shoply/coupon/claimAllBySeller", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
                "X-Requested-With": "XMLHttpRequest" //  AJAX 요청임을 명시
            },
            body: new URLSearchParams({ seller_id: sellerId })
        });

        const result = await response.text();

        //  HTML 응답 감지 (로그인 리다이렉트 시 HTML 전체가 넘어옴)
        if (result.includes("<!DOCTYPE html")) {
            alert("로그인이 필요합니다.");
            location.href = "/shoply/member/login";
            return;
        }

        if (result === "login_required") {
            alert("로그인이 필요합니다.");
            location.href = "/shoply/member/login";
        } else if (result === "no_coupons") {
            alert("해당 판매자가 발급한 쿠폰이 없습니다.");
        } else if (result === "already_claimed") {
            alert("이미 해당 판매자의 쿠폰을 모두 받았습니다.");
        } else if (result === "success") {
            alert("해당 판매자의 모든 쿠폰이 발급되었습니다!");
        } else {
            alert("쿠폰 발급 중 오류가 발생했습니다.");
        }
    });
});
