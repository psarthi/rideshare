package com.digitusrevolution.rideshare.user.domain;

import java.util.ArrayList;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.common.inf.GenericDAO;
import com.digitusrevolution.rideshare.model.user.data.StateEntity;
import com.digitusrevolution.rideshare.model.user.domain.State;

public class StateDO implements DomainObjectPKInteger<State>{
	
	private State state;
	private final GenericDAO<StateEntity, Integer> genericDAO = new GenericDAOImpl<>(StateEntity.class);

	@Override
	public List<State> getAll() {
		List<State> states = new ArrayList<>();
		List<StateEntity> stateEntities = genericDAO.getAll();
		for (StateEntity stateEntity : stateEntities) {
			State state = new State();
			state.setEntity(stateEntity);
			states.add(state);
		}
		return states;
	}

	@Override
	public void update(State state) {
		if (state.getId()==0){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+state.getId());
		}
		genericDAO.update(state.getEntity());				
	}

	@Override
	public int create(State state) {
		int id = genericDAO.create(state.getEntity());
		return id;
	}

	@Override
	public State get(int id) {
		state = new State();
		StateEntity stateEntity = genericDAO.get(id);
		if (stateEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		state.setEntity(stateEntity);
		return state;
	}

	@Override
	public void delete(int id) {
		state = get(id);
		genericDAO.delete(state.getEntity());			
	}
	
}
