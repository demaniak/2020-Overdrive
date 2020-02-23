package za.co.entelect.challenge.game.contracts.renderer

import net.liftweb.json._
import net.liftweb.json.JsonDSL._

import za.co.entelect.challenge.game.contracts.game.GamePlayer
import za.co.entelect.challenge.game.contracts.map.GameMap
import za.co.entelect.challenge.game.contracts.map.CarGameMap
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer

class JsonRenderer extends BaseMapRenderer {

    override def renderFragment(gameMap: CarGameMap, gamePlayer: CarGamePlayer): String = {
        val mapFragment = gameMap.getMapFragment(gamePlayer);
        val mapFragmentPlayer = mapFragment.getPlayer();
        val mapFragmentPlayerPosition = mapFragmentPlayer.getPosition();
        val mapFragmentJsonStructure = 
            ("currentRound" -> mapFragment.getCurrentRound()) ~
            ("maxRounds" -> 1500) ~
            ("player" ->
                ("id" -> mapFragmentPlayer.getId()) ~
                ("position" ->
                    ("lane" -> mapFragmentPlayerPosition.getLane()) ~
                    ("blockNumber" -> mapFragmentPlayerPosition.getBlockNumber())
                ) ~
                ("speed" -> mapFragmentPlayer.getSpeed()) ~
                ("state" -> mapFragmentPlayer.getState())
            ) ~
            ("lanes" -> 
                mapFragment.getLanes().toList.map 
                {
                    l => 
                    ("position" ->
                        ("lane" -> l.getPosition().getLane()) ~
                        ("blockNumber" -> l.getPosition().getBlockNumber())
                    ) ~
                    ("object" -> l.getMapObject()) ~
                    ("occupiedByPlayerWithId" -> l.getOccupiedByPlayerWithId())
                }
            );
        
        val jsonString = prettyRender(mapFragmentJsonStructure);
        return jsonString;
    }

    override def renderVisualiserMap(gameMap: CarGameMap) : String = {
        val globalMapJsonStructure = 
            ("players" -> 
                gameMap.getCarGamePlayers().toList.map 
                {
                    x => 
                    ("id" -> x.getGamePlayerId()) ~
                    ("position" ->
                        ("lane" -> gameMap.getPlayerBlockPosition(x.getGamePlayerId()).getLane()) ~
                        ("blockNumber" -> gameMap.getPlayerBlockPosition(x.getGamePlayerId()).getBlockNumber())
                    ) ~
                    ("blockrate" -> x.getSpeed()) ~
                    ("state" -> x.getState())
                }
            ) ~
            ("blocks" -> 
                gameMap.getBlocks().toList.map
                {
                    l => 
                    ("position" ->
                        ("lane" -> l.getPosition().getLane()) ~
                        ("blockNumber" -> l.getPosition().getBlockNumber())
                    ) ~
                    ("object" -> l.getMapObject()) ~
                    ("occupied-by-player-with-id" -> l.getOccupiedByPlayerWithId())
                }
            );
        val jsonString = prettyRender(globalMapJsonStructure);
        return jsonString;
    }
}