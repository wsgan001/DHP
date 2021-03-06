package org.eltech.ddm.associationrules.apriori.dhp.steps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eltech.ddm.associationrules.Transaction;
import org.eltech.ddm.associationrules.TransactionList;
import org.eltech.ddm.associationrules.apriori.dhp.DHPMiningModel;
import org.eltech.ddm.inputdata.MiningInputStream;
import org.eltech.ddm.miningcore.MiningException;
import org.eltech.ddm.miningcore.algorithms.Step;
import org.eltech.ddm.miningcore.miningfunctionsettings.EMiningFunctionSettings;
import org.eltech.ddm.miningcore.miningmodel.EMiningModel;

public class PruneTransactionListStep extends Step{

	public PruneTransactionListStep(EMiningFunctionSettings settings) throws MiningException {
		super(settings);
	}

	@Override
	protected EMiningModel execute(MiningInputStream inputData, EMiningModel model) throws MiningException {
		DHPMiningModel modelA = (DHPMiningModel) model;
		TransactionList transactionList = modelA.getTransactionList();
		Transaction transaction = transactionList.get(modelA.getCurrentTransaction());
		int index = modelA.getCurrentLargeItemSets();
		Map<List<String>, Integer> map = modelA.getItemSetsHashTable().get(index);
		if (map != null) {
			if (isNeedPrune(map, transaction, index)) {
				removeTransaction(transaction, transactionList);
				modelA.setCurrentTransaction(modelA.getCurrentTransaction() - 1); 
				modelA.setTransactionPruned(true);
			} else {
				modelA.setTransactionPruned(false);
			}
		}
		
		return modelA;
	}
	
	public boolean isNeedPrune(Map<List<String>, Integer> map, Transaction transaction, int k) {
		List<String> transactionItemIDList = transaction.getItemIDList();
		if (transactionItemIDList.size() < k) {
			return true;
		}

		int indexes[] = new int[k];
		for (int i = 0; i < indexes.length; i++) {
			indexes[i] = i;
		}

		boolean flag = true;
		while (flag) {
			List<String> elements = new ArrayList<String>();
			for (int i : indexes) {
				elements.add(transactionItemIDList.get(i));
			}
			elements.sort(null);
			if (map.containsKey(elements)) {
				return false;
			}

			int n = 1;
			while (flag) {
				indexes[indexes.length - n]++;
				if (indexes[indexes.length - n] < transactionItemIDList.size() - n + 1) {
					for (int h = indexes.length - n + 1; h < indexes.length; h++) {
						indexes[h] = indexes[h - 1] + 1;
					}
					n = 1;
					break;
				} else {
					n++;
					if (n == k + 1) {
						flag = false;
					}
				}
			}
		}
		return true;
	}
	
	private boolean removeTransaction(Transaction transaction, TransactionList transactionList) {
		return transactionList.remove(transaction);
	}

}
