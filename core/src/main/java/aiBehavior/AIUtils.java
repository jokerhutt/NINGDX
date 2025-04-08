package aiBehavior;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import entities.Entity;
import entities.NPC;
import entities.Player;
import utils.DirectionUtils;

import java.util.List;

public class AIUtils {

    public AIUtils () {

    }

    public static void chooseRandomDirection(NPC npc) {
        int i = MathUtils.random(0, 3);
        switch (i) {
            case 0:
                npc.intendedDirection.set(1, 0);
                npc.lastDirection.set(1, 0);
                break;
            case 1:
                npc.intendedDirection.set(-1, 0);
                npc.lastDirection.set(-1, 0);

                break;
            case 2:
                npc.intendedDirection.set(0, 1);
                npc.lastDirection.set(0, 1);

                break;
            case 3:
                npc.intendedDirection.set(0, -1);
                npc.lastDirection.set(0, -1);
                break;
        }
    }


    public static void followEntity(NPC npc, Entity entity, boolean[][] walkableGrid) {
        float tileSize = 1f;
        Vector2 npcTile = worldToTile(npc.getPosition(), tileSize);
        Vector2 playerTile = worldToTile(entity.getPosition(), tileSize);

        List<Vector2> path = AStar.findPath(npcTile, playerTile, walkableGrid);
        if (path != null) {
            npc.path.clear();
            npc.path.addAll(path.toArray(new Vector2[0]));
            npc.currentPathIndex = 0;
        }
        if (path != null && !path.isEmpty()) {
            Vector2 nextTile = path.get(0);
            Vector2 worldTarget = tileToWorld(nextTile, tileSize);
            Vector2 direction = worldTarget.cpy().sub(npc.getPosition()).nor();
            npc.intendedDirection.set(direction);
            npc.lastDirection.set(direction);
        } else {
            npc.intendedDirection.set(entity.getPosition().cpy().sub(npc.getPosition()).nor());
            npc.lastDirection.set(npc.intendedDirection);
        }
//        System.out.println("Chasing: " + npc.chasing);
//        System.out.println("NPC Pos: " + npc.getPosition() + " -> Player Pos: " + entity.getPosition());
//        System.out.println("NPC Tile: " + npcTile + " Player Tile: " + playerTile);
//        System.out.println("Path size: " + (path != null ? path.size() : "null"));
    }

    public static Vector2 worldToTile(Vector2 worldPos, float tileSize) {
        return new Vector2((int)(worldPos.x / tileSize), (int)(worldPos.y / tileSize));
    }

    public static Vector2 tileToWorld(Vector2 tilePos, float tileSize) {
        return new Vector2(tilePos.x * tileSize, tilePos.y * tileSize);
    }

    public static void handleChasing(float delta, NPC npc) {
        npc.pathRefreshTimer += delta;

        if (npc.pathRefreshTimer >= npc.pathRefreshCooldown) {
            followEntity(npc, npc.lockedOnto, npc.screen.walkableGrid);
            npc.pathRefreshTimer = 0f;
        }

        npc.moving = true;

        if (npc.path != null && npc.currentPathIndex < npc.path.size) {
            Vector2 tile = npc.path.get(npc.currentPathIndex);
            Vector2 targetWorld = new Vector2(tile.x + 0.5f, tile.y + 0.5f);
            Vector2 direction = targetWorld.cpy().sub(npc.position).nor();
            npc.velocity.set(direction.scl(npc.speed));

            if (npc.position.dst(targetWorld) < 0.1f) {
                npc.currentPathIndex++;
            }
        } else {
            npc.velocity.set(0, 0);
        }
    }

    public static void determineNearestEntity (NPC npc, Player player) {
            float distanceFromPlayer = DirectionUtils.findDistance(player.position, npc.position);
            float nearestOtherDistance = 100f;

            NPC currNpc;
            for (int i = 0; i < npc.screen.npcArray.size; i++) {
                currNpc = (NPC)npc.screen.npcArray.get(i);
                float currentDistance;
                if (currNpc != null) {
                    if (i == 0) {
                        currentDistance = DirectionUtils.findDistance(currNpc.position, npc.position);
                        nearestOtherDistance = currentDistance;
                        npc.lockedOnto = currNpc;
                    } else {
                        currentDistance = DirectionUtils.findDistance(currNpc.position, npc.position);
                        if (currentDistance < nearestOtherDistance) {
                            nearestOtherDistance = currentDistance;
                            npc.lockedOnto = currNpc;
                        }
                    }
                }
            }

            if (distanceFromPlayer > 0f || nearestOtherDistance > 0f
                && distanceFromPlayer < 4f || nearestOtherDistance < 4f) {

                if (distanceFromPlayer < nearestOtherDistance) {
                    npc.lockedOnto = player;
                }
                npc.chasing = true;

            } else {
                npc.lockedOnto = null;
                npc.chasing = false;
            }
    }

    public static void determineNearestEnemy (NPC npc) {
        float nearestOtherDistance = 100f;

        NPC currNpc;
        for (int i = 0; i < npc.screen.enemyArray.size; i++) {
            currNpc = (NPC)npc.screen.enemyArray.get(i);
            float currentDistance;
            if (currNpc != null) {
                if (i == 0) {
                    currentDistance = DirectionUtils.findDistance(currNpc.position, npc.position);
                    nearestOtherDistance = currentDistance;
                    npc.lockedOnto = currNpc;
                } else {
                    currentDistance = DirectionUtils.findDistance(currNpc.position, npc.position);
                    if (currentDistance < nearestOtherDistance) {
                        nearestOtherDistance = currentDistance;
                        npc.lockedOnto = currNpc;
                    }
                }
            }
        }

        if (nearestOtherDistance < 4f) {

            npc.chasing = true;

        } else {
            npc.lockedOnto = null;
            npc.chasing = false;
        }
    }





}
