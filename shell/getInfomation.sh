#!/bin/bash

path_file_data_test=/home/duongvantien/Desktop/data_test.txt
path_file_infomation=/home/duongvantien/Desktop/FileAction/shell/infomation.txt

fromDate=$1
toDate=$2

echo -e "from_line="`grep -n $fromDate $path_file_data_test` >> $path_file_infomation
echo -e "to_line="`grep -n $toDate $path_file_data_test` >> $path_file_infomation
echo -e "number_lines="`wc -l < $path_file_data_test` >> $path_file_infomation
