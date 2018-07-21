package com.iota.iri.storage;

import com.iota.iri.hash.Curl;
import com.iota.iri.hash.Sponge;
import com.iota.iri.hash.SpongeFactory;
import com.iota.iri.model.Hash;
import com.iota.iri.model.Tag;
import com.iota.iri.model.Transaction;
import com.iota.iri.storage.rocksDB.RocksDBPersistenceProvider;
import com.iota.iri.utils.Converter;
import com.iota.iri.controllers.TransactionViewModel;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.util.Arrays;
import java.util.Random;
import java.util.Set;

import static com.iota.iri.controllers.TransactionViewModelTest.getRandomTransactionTrits;
import static org.junit.Assert.assertTrue;

public class TangleTest {
    private final TemporaryFolder dbFolder = new TemporaryFolder();
    private final TemporaryFolder logFolder = new TemporaryFolder();
    private Tangle tangle = new Tangle();

    private static final Random seed = new Random();

    @Before
    public void setUp() throws Exception {
        TemporaryFolder dbFolder = new TemporaryFolder(), logFolder = new TemporaryFolder();
        dbFolder.create();
        logFolder.create();
        RocksDBPersistenceProvider rocksDBPersistenceProvider;
        rocksDBPersistenceProvider = new RocksDBPersistenceProvider(dbFolder.getRoot().getAbsolutePath(),
                logFolder.getRoot().getAbsolutePath(),1000);
        tangle.addPersistenceProvider(rocksDBPersistenceProvider);
        tangle.init();
    }

    @After
    public void tearDown() throws Exception {
        tangle.shutdown();
    }

    @Test
    public void save() throws Exception {
    }

    @Test
    public void getKeysStartingWithValue() throws Exception {
        byte[] trits = getRandomTransactionTrits();
        TransactionViewModel transactionViewModel = new TransactionViewModel(trits, Hash.calculate(SpongeFactory.Mode.CURLP81, trits));
        transactionViewModel.store(tangle);
        Set<Indexable> tag = tangle.keysStartingWith(Tag.class, Arrays.copyOf(transactionViewModel.getTagValue().bytes(), 15));
        Assert.assertNotEquals(tag.size(), 0);
    }

    @Test
    public void get() throws Exception {
    }

    public static byte[] getRandomTransactionTrits() {
        byte[] out = new byte[TransactionViewModel.TRINARY_SIZE];

        for(int i = 0; i < out.length; i++) {
            out[i] = (byte) (seed.nextInt(3) - 1);
        }

        return out;
    }

}