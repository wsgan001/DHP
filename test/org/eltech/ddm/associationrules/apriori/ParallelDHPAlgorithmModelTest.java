package org.eltech.ddm.associationrules.apriori;

import org.eltech.ddm.associationrules.AssociationRulesFunctionSettings;
import org.eltech.ddm.associationrules.apriori.dhp.ParallelDHPAlgorithm;
import org.eltech.ddm.handlers.ExecutionSettings;
import org.eltech.ddm.handlers.thread.MultiThreadedExecutionEnvironment;
import org.eltech.ddm.inputdata.DataSplitType;
import org.eltech.ddm.inputdata.MiningInputStream;
import org.eltech.ddm.miningcore.MiningException;
import org.eltech.ddm.miningcore.miningfunctionsettings.DataProcessingStrategy;
import org.eltech.ddm.miningcore.miningfunctionsettings.MiningModelProcessingStrategy;
import org.eltech.ddm.miningcore.miningtask.EMiningBuildTask;

public class ParallelDHPAlgorithmModelTest extends AprioriModelTest{

	@Override
	protected AprioriMiningModel buildModel(AssociationRulesFunctionSettings miningSettings,
			MiningInputStream inputData) throws MiningException {
		miningSettings.getAlgorithmSettings().setDataSplitType(DataSplitType.block);
		miningSettings.getAlgorithmSettings().setDataProcessingStrategy(DataProcessingStrategy.SeparatedDataSet);
		miningSettings.getAlgorithmSettings()
				.setModelProcessingStrategy(MiningModelProcessingStrategy.SeparatedMiningModel);
		miningSettings.getAlgorithmSettings().setNumberHandlers(2);

		ExecutionSettings executionSettings = new ExecutionSettings();
//		executionSettings.setNumberHandlers(2);

		MultiThreadedExecutionEnvironment environment = new MultiThreadedExecutionEnvironment(executionSettings);

		ParallelDHPAlgorithm algorithm = new ParallelDHPAlgorithm(miningSettings);

		EMiningBuildTask buildTask = new EMiningBuildTask();
		buildTask.setInputStream(inputData);
		buildTask.setMiningAlgorithm(algorithm);
		buildTask.setMiningSettings(miningSettings);
		buildTask.setExecutionEnvironment(environment);
		AprioriMiningModel model = (AprioriMiningModel) buildTask.execute();

		return model;
	}

}
