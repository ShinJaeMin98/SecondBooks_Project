package org.choongang.board.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.board.service.SaveBoardDataService;
import org.choongang.commons.rests.JSONData;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class ApiBoardController {

    private final SaveBoardDataService saveBoardDataService;

    @GetMapping("/save_post/{bSeq}")
    public JSONData<Object> savePost(@PathVariable("bSeq") Long bSeq) {
        saveBoardDataService.save(bSeq);
        saveBoardDataService.getTotalCount(bSeq);

        return new JSONData<>();
    }

    @DeleteMapping("/save_post/{bSeq}")
    public JSONData<Object> deleteSavePost(@PathVariable("bSeq") Long bSeq) {
        saveBoardDataService.delete(bSeq);
        saveBoardDataService.getTotalCount(bSeq);
        return new JSONData<>();
    }
}