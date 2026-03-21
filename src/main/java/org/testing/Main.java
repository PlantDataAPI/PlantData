package org.testing;


import org.testing.PlantDataModule.Dispatch;
import org.testing.PlantDataModule.IronAndPacking;
import org.testing.PlantDataModule.Processing;

public class Main {
    public static void main(String[] args)
    {
        Processing.getProcessingData();
        IronAndPacking.getIronAndPackingData();
        Dispatch.getDispatchData();
    }
}