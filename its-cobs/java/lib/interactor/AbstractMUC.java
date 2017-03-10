package lib.interactor;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import lib.dao.AbstractDAO;

public class AbstractMUC<M, ID extends Serializable> implements AbstractDAO<M, ID> {
	protected Class<M> persistentClass;

	// @Autowired
	// protected HibernateTemplate hibernateTemplate;

	@Autowired
	protected SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	public AbstractMUC() {
		persistentClass = (Class<M>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
	}

	//@Override
	@SuppressWarnings("unchecked")
	public M findUserById(final ID id) throws DataAccessException {
		return (M) getSession().get(persistentClass, id);
	}

	//@Override
	@SuppressWarnings("unchecked")
	public void persist(final M... models) throws DataAccessException {
		for (M each : models) {

			getSession().saveOrUpdate(each);
		}
	}

	//@Override
	@SuppressWarnings("unchecked")
	public void update(final M... models) throws DataAccessException {
		for (M each : models) {
			getSession().update(each);
		}
	}

	//@Override
	@SuppressWarnings("unchecked")
	public void save(final M... models) throws DataAccessException {
		for (M each : models) {
			getSession().save(each);
		}
	}

	//@Override
	@SuppressWarnings("unchecked")
	public void deleteById(final ID... ids) throws DataAccessException {
		for (ID each : ids) {

			getSession().delete(findUserById(each));
		}
	}

	@SuppressWarnings("unchecked")
	public List<M> findAll() {
		return (List<M>) getSession().createSQLQuery("SELECT * FROM " + persistentClass.getSimpleName()).list();
		// return (List<M>) hibernateTemplate.find("FROM " +
		// persistentClass.getSimpleName());

	}

	@SuppressWarnings("unchecked")
	public List<M> findByName(String name) {
		return (List<M>) getSession().createQuery(" FROM " + persistentClass.getSimpleName() + "WHERE NAME = " + name)
				.list();
		// return (List<M>) hibernateTemplate.find("FROM " +
		// persistentClass.getSimpleName());

	}

	protected Session getSession() {
		Session session = null;
		try {
			// Step-2: Implementation
			session = sessionFactory.getCurrentSession();
		} catch (HibernateException e) {
			// Step-3: Implementation
			session = sessionFactory.openSession();
		}
		return session;
	}

	protected DetachedCriteria getDetachedCriteria() {
		return DetachedCriteria.forClass(persistentClass);
	}

	protected Criteria createCriteria() {
		return getSession().createCriteria(persistentClass);
	}

	public Integer countAll() {
		return (Integer) this.createCriteria().setProjection(Projections.rowCount()).uniqueResult();
	}
}
