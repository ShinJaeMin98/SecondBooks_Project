var commonLib = commonLib || {};

commonLib.followLib = {
    /**
    * 팔로우
    *
    * @param userId : 사용자 ID
    * @param callback : 콜백 함수
    */
    follow(userId, callback) {

        const { ajaxLoad } = commonLib;

        ajaxLoad("GET", `/api/member/follow/${userId}`)
            .then(res => {
                if (typeof callback == 'function') {
                    callback();
                }
            })
            .catch(err => console.error(err));
    },
    /**
    * 언팔로우
    *
    * @param userId : 사용자 ID
    */
    unfollow(userId, callback) {
        const { ajaxLoad } = commonLib;

        ajaxLoad("GET", `/api/member/unfollow/${userId}`)
            .then(res => {
                if (typeof callback == 'function') {
                    callback();
                }
            })
            .catch(err => console.error(err));
    }
};


window.addEventListener("DOMContentLoaded", function() {
    const followings = document.getElementsByClassName("follow_action");
    const { follow, unfollow } = commonLib.followLib;

    // 팔로잉, 언팔로잉 처리
    for(const el of followings) {
        el.addEventListener("click", function() {
            const classList = this.classList;
            const action = classList.contains("unfollow") ? unfollow : follow;
            action(this.dataset.userId, function() {
                    if (classList.contains("unfollow")) {
                        classList.remove("unfollow")
                        el.innerText = "Follow";
                    } else {
                        classList.add("unfollow");
                        el.innerText = "UnFollow";
                    }
            });
        });
    }
});