package org.choongang.board.service;

import lombok.RequiredArgsConstructor;
import org.choongang.board.entities.BoardData;
import org.choongang.board.repositories.BoardDataRepository;
import org.choongang.file.service.FileDeleteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardDeleteService {
    private final BoardDataRepository boardDataRepository;
    private final BoardInfoService boardInfoService;
    private final FileDeleteService fileDeleteService;

    /**
     *  게시글 삭제
     *
     * @param seq
     */
    public void delete(Long seq) {
        BoardData data = boardInfoService.get(seq);

        String gid = data.getGid();

        boardDataRepository.delete(data);
        boardDataRepository.flush();

        // 업로드된 파일 삭제
        fileDeleteService.delete(gid);
    }
}
