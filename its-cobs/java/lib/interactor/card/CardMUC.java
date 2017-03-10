package lib.interactor.card;

import org.springframework.stereotype.Repository;

import lib.dao.CardDAO;
import lib.entity.Card;
import lib.entity.Token;
import lib.interactor.AbstractMUC;

@Repository
public class CardMUC extends AbstractMUC<Token, Integer> implements CardDAO {

	@Override
	public Card getCard(String code) {
		String hql = " FROM Card WHERE code = :code";
		return (Card) getSession().createQuery(hql).setParameter("code", code).uniqueResult();
	}

}
