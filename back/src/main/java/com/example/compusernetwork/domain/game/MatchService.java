package com.example.compusernetwork.domain.game;

import com.example.compusernetwork.domain.member.Member;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MatchService {
    private Queue<Member> waitingQueue = new LinkedList<>();
    private Map<String, GameRoom> gameRooms = new ConcurrentHashMap<>();

    public synchronized GameRoom match(Member member) {
        if (waitingQueue.isEmpty()) {
            waitingQueue.add(member);
            return null;
        } else {
            if (waitingQueue.contains(member)) return null;
            Member opponent = waitingQueue.poll();
            GameRoom gameRoom = new GameRoom(member, opponent);
            gameRooms.put(gameRoom.getRoomId(), gameRoom);
            return gameRoom;
        }
    }

    public GameRoom getGameRoom(String roomId) {
        return gameRooms.get(roomId);
    }
}

