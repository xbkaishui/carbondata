package org.carbondata.integration.spark.testsuite.dataload

import org.apache.spark.sql.common.util.CarbonHiveContext._
import org.apache.spark.sql.common.util.QueryTest
import org.apache.spark.sql.Row
import org.scalatest.BeforeAndAfterAll

/**
 * Test Class for data loading when there are null measures in data
 *
 */
class TestLoadDataWithEmptyArrayColumns extends QueryTest with BeforeAndAfterAll {
  override def beforeAll {
    sql("drop table if exists nest13")
    sql("""
           CREATE TABLE nest13 (imei string,age int,
           productdate timestamp,gamePointId double,
           reserved6 array<string>,mobile struct<poc:string, imsi:int>)
           STORED BY 'org.apache.carbondata.format'
        """)
  }

  test("test carbon table data loading when there are empty array columns in data") {
    sql(s"""
            LOAD DATA inpath './src/test/resources/arrayColumnEmpty.csv'
            into table nest13 options ('DELIMITER'=',', 'complex_delimiter_level_1'='/',
            'FILEHEADER'= 'imei,age,productdate,gamePointId,reserved6,mobile')
         """)
    checkAnswer(
      sql("""
             SELECT count(*) from nest13
          """),
      Seq(Row(20)))
  }

  override def afterAll {
    sql("drop table nest13")
  }
}
