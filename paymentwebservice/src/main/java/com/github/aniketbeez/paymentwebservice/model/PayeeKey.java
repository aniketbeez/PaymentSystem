package com.github.aniketbeez.paymentwebservice.model;

import java.io.Serializable;
import java.util.UUID;

public class PayeeKey implements Serializable {
    private UUID userId;
    private UUID payeeId;
}
