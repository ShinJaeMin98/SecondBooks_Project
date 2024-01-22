const boardLib = {
    /**
    * 게시글 찜하기
    *   - 로그인 회원 정보를 가지고 저장

    * @param bSeq : 게시글 번호
    *
    */
    save(bSeq) {
        const { ajaxLoad } = commonLib;

        ajaxLoad('GET', `/api/board/save_post/${bSeq}`);

    },
    /**
    * 찜하기 해제
    *
    * @param bSeq : 게시글 번호
    */
    deleteSave(bSeq) {

        const { ajaxLoad } = commonLib;

        ajaxLoad('DELETE', `/api/board/save_post/${bSeq}`);
    }
}

window.addEventListener("DOMContentLoaded", function() {
    /* 찜하기 처리 S */
    const savePosts = document.getElementsByClassName("save_post");
    for (const el of savePosts) {
        el.addEventListener("click", function() {
            // data-seq : 게시글 번호
            // save_post에 on 클래스가 포함되어 있는 경우 -> 찜 한 상태, on -> 찜을 아직 안한 상태
            const bSeq = this.dataset.seq;
            if (this.classList.contains('on')) { // 찜한 상태 -> 해제
                boardLib.deleteSave(bSeq);

                this.classList.remove('on');

            } else { // 찜을 아직 안한 상태 -> 찜하기
                boardLib.save(bSeq);
                this.classList.add('on');
            }

            if (this.dataset.refresh == 'true') {
                location.reload();
            }
        });
    }

    /* 찜하기 처리 E */


});