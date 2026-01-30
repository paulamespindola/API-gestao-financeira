package br.com.gestaofinanceira.transaction_processor.application.gateway;

import br.com.gestaofinanceira.transaction_processor.application.dto.AccountData;
import br.com.gestaofinanceira.transaction_processor.application.dto.AccountUpdateDto;

public interface AccountClient {
    AccountData getAccount(Long userId);
    //AccountData updateAccount(AccountUpdateDto dto, Long userId);
}
