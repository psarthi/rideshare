package com.digitusrevolution.rideshare.common.mapper.user;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.model.user.data.StateEntity;
import com.digitusrevolution.rideshare.model.user.domain.State;

public class StateMapper implements Mapper<State, StateEntity>{

	@Override
	public StateEntity getEntity(State state, boolean fetchChild) {
		StateEntity stateEntity = new StateEntity();
		stateEntity.setId(state.getId());
		stateEntity.setName(state.getName());
		CityMapper cityMapper = new CityMapper();
		stateEntity.setCities(cityMapper.getEntities(stateEntity.getCities(), state.getCities(), fetchChild));
		return stateEntity;
	}

	@Override
	public StateEntity getEntityChild(State state, StateEntity entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public State getDomainModel(StateEntity stateEntity, boolean fetchChild) {
		State state = new State();
		state.setId(state.getId());
		state.setName(state.getName());
		CityMapper cityMapper = new CityMapper();
		state.setCities(cityMapper.getDomainModels(state.getCities(), stateEntity.getCities(), fetchChild));
		return state;
	}

	@Override
	public State getDomainModelChild(State state, StateEntity stateEntity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<State> getDomainModels(Collection<State> states, Collection<StateEntity> stateEntities,
			boolean fetchChild) {
		for (StateEntity stateEntity : stateEntities) {
			State state = new State();
			state = getDomainModel(stateEntity, fetchChild);
			states.add(state);
		}
		return states;
	}

	@Override
	public Collection<StateEntity> getEntities(Collection<StateEntity> stateEntities, Collection<State> states,
			boolean fetchChild) {
		for (State state : states) {
			StateEntity stateEntity = new StateEntity();
			stateEntity = getEntity(state, fetchChild);
			stateEntities.add(stateEntity);
		}
		return stateEntities;
	}

}
