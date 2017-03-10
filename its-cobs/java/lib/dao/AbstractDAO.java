package lib.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.dao.DataAccessException;

public interface AbstractDAO<M, ID extends Serializable> {

	M findUserById(ID id) throws DataAccessException;

	@SuppressWarnings("unchecked")
	void persist(M... models) throws DataAccessException;

	@SuppressWarnings("unchecked")
	void deleteById(ID... ids) throws DataAccessException;

	List<M> findAll();

	Integer countAll();

	@SuppressWarnings("unchecked")
	void update(M... models) throws DataAccessException;

	@SuppressWarnings("unchecked")
	void save(M... models) throws DataAccessException;
	List<M> findByName(String name);
}
