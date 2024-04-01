package com.vado.sic.run;

import com.vado.sic.crypto.*;
import lombok.SneakyThrows;

public class MainRunner {

    @SneakyThrows
    public static void main(String[] args) {
        AESCrypto.main(args);
        BlowfishCrypto.main(args);
        CamelliaCrypto.main(args);
        ChaCha20Crypto.main(args);
        DESCrypto.main(args);
        GOSTCrypto.main(args);
        RC4Crypto.main(args);
        SEEDCrypto.main(args);
        SerpentCrypto.main(args);
    }
}