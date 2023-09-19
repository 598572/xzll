package com.xzll.test.service;


import com.xzll.test.ao.PrePayOrderAo;
import com.xzll.test.entity.PrePayOrderRecordDO;

import java.util.List;

public interface PrePayOrderRecordService {


	void prePayOrder(PrePayOrderAo ao);

	List<PrePayOrderRecordDO> findForUpdate(PrePayOrderAo ao);

}
