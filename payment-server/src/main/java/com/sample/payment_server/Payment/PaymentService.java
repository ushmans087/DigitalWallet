package com.sample.payment_server.Payment;

import com.sample.payment_server.Transactions.TransferEntity;
import com.sample.payment_server.Transactions.TransferRepo;
import com.sample.payment_server.Transactions.TransferStatus;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    private final RestClient restClient;
    private final TransferRepo transferRepo;

    public PaymentService(TransferRepo transferRepo) {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8086")
                .build();
        this.transferRepo = transferRepo;
    }

    public WalletResponseDTO getSender(String email) {
        return restClient.get()
                .uri("/wallet/findby/email/{email}", email)
                .retrieve()
                .body(WalletResponseDTO.class);
    }

    public WalletResponseDTO getReceiver(Long receiverId) {
        return restClient.get()
                .uri("/wallet/findby/id/{id}", receiverId)
                .retrieve()
                .body(WalletResponseDTO.class);
    }

    private String updateWallet(WalletUpdateRequestDTO wallet) {

        return restClient.put()
                .uri("/wallet/update/balance")
                .body(wallet)
                .retrieve()
                .body(String.class);
    }

    @Transactional
    public String transfer(String senderEmail,Long receiverWalletId,double amount,String description) {

        WalletResponseDTO sender = getSender(senderEmail);
        WalletResponseDTO receiver = getReceiver(receiverWalletId);

        if(sender == null || receiver == null){
            return "Not a Valid sender or receiver";
        }

        if(sender.getAmount() < amount){
            return "Insufficient funds";
        }

        if(sender.getUserId().equals(receiver.getUserId())){
            return "you can not transfer money to yourself";
        }

        TransferEntity transaction = new TransferEntity(
                sender.getWalletId(),
                receiver.getWalletId(),
                sender.getUsername(),
                receiver.getUsername(),
                sender.getUserId(),
                receiver.getUserId(),
                amount,
                LocalDateTime.now(),
                TransferStatus.PENDING,
                description
        );

        try{

            double senderAmount = sender.getAmount() - amount;
            double receiverAmount = receiver.getAmount() + amount;

            WalletUpdateRequestDTO senderWallet =
                    new WalletUpdateRequestDTO(sender.getWalletId(), senderAmount);

            WalletUpdateRequestDTO reciverWallet =
                    new WalletUpdateRequestDTO(receiver.getWalletId(), receiverAmount);

            String senderResponse = updateWallet(senderWallet);
            String receiverResponse = updateWallet(reciverWallet);

            transaction.setStatus(TransferStatus.SUCCESS);
            transferRepo.save(transaction);

            return "Transaction Completed Successfully";

        }catch(Exception e){
            e.printStackTrace();

            transaction.setStatus(TransferStatus.FAILED);
            transferRepo.save(transaction);

            return "Transaction Failed";
        }
    }
}