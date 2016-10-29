#!/bin/bash

for i in 0 1 2 3 4
do
    sysbench --test=cpu run > sysbench_cpu_round_$i.txt
done

for i in 0 1 2 3 4
do
    sysbench --test=memory run > sysbench_mem_round_$i.txt
done

for i in 0 1 2 3 4
do
    sysbench --test=fileio --file-test-mode=seqrd run > sysbench_fs_seqrd_round_$i.txt
done

for i in 0 1 2 3 4
do
    sysbench --test=fileio --file-test-mode=seqwr run > sysbench_fs_seqwr_round_$i.txt
done

for i in 0 1 2 3 4
do
    sysbench --test=fileio --file-test-mode=rndrd run > sysbench_fs_rndrd_round_$i.txt
done

for i in 0 1 2 3 4
do
    sysbench --test=fileio --file-test-mode=rndwr run > sysbench_fs_rndwr_round_$i.txt
done

