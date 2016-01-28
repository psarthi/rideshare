package com.digitusrevolution.rideshare.user.domain;

import java.util.ArrayList;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.common.inf.GenericDAO;
import com.digitusrevolution.rideshare.common.mapper.user.StateMapper;
import com.digitusrevolution.rideshare.model.user.data.StateEntity;
import com.digitusrevolution.rideshare.model.user.domain.State;

public class StateDO implements DomainObjectPKInteger<State>{
	
	private State state;
	private StateEntity stateEntity;
	private StateMapper stateMapper;
	private final GenericDAO<StateEntity, Integer> genericDAO;
	
	public StateDO() {
		state = new State();
		stateEntity = new StateEntity();
		stateMapper = new StateMapper();
		genericDAO = new GenericDAOImpl<>(StateEntity.class);
	}

	public void setState(State state) {
		this.state = state;
		stateEntity = stateMapper.getEntity(state, true);
	}

	public void setStateEntity(StateEntity stateEntity) {
		this.stateEntity = stateEntity;
		state = stateMapper.getDomainModel(stateEntity, false);
	}

	@Override
	public List<State> getAll() {
		List<State> states = new ArrayList<>();
		List<StateEntity> stateEntities = genericDAO.getAll();
		for (StateEntity stateEntity : stateEntities) {
			setStateEntity(stateEntity);
			states.add(state);
		}
		return states;
	}

	@Override
	public void update(State state) {
		if (state.getId()==0){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+state.getId());
		}
		setState(state);
		genericDAO.update(stateEntity);				
	}

	@Override
	public void fetchChild() {
		state = stateMapper.getDomainModelChild(state, stateEntity);	
	}

	@Override
	public int create(State state) {
		setState(state);
		int id = genericDAO.create(stateEntity);
		return id;
	}

	@Override
	public State get(int id) {
		stateEntity = genericDAO.get(id);
		if (stateEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		setStateEntity(stateEntity);
		return state;
	}

	@Override
	public State getChild(int id) {
		get(id);
		fetchChild();
		return state;
	}

	@Override
	public void delete(int id) {
		state = get(id);
		setState(state);
		genericDAO.delete(stateEntity);			
	}
	
}
