package org.choongang.community.freeBoard.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FreeBoardService {

    private final FreeBoardRepository freeBoardRepository;

//    public FreeBoard getFreeBoard(Long seq){
//        Optional<FreeBoard> freeboard = this.freeBoardRepository.findById(seq);
//        if(freeboard.isPresent()){
//
//            FreeBoard freeBoard = freeboard.get();
//
//        }
//    }
}
