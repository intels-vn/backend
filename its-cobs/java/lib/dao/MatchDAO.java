package lib.dao;

import java.util.List;

import lib.entity.Matcheess;

public interface MatchDAO extends AbstractDAO<Matcheess, Long>{

	List<Matcheess> getMatchInfo(Long id);

}
