package nlp

import org.apache.spark.sql.{Encoders, SparkSession}
import org.apache.spark.sql.functions._

object NLPMain {
  case class DataSuicides (country: String,
                           year: String,
                           sex: String,
                           age: String,
                           suicides_no: Float)

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("ECommerce Data")
      .getOrCreate()

    // read
    var df = spark.read
      .option("header", "true")
      .schema(Encoders.product[DataSuicides].schema)
      .csv("/user/hjmoon/master.csv")

    df.printSchema()

    // transform
    df = df.withColumn("constant_1", lit(1))
      .withColumn("id", monotonically_increasing_id())

    df.createOrReplaceTempView("df")

    val sui_per_year = spark.sql("""
                                select year, sum(suicides_no) sum_sui, round(avg(suicides_no), 1) avg_sui
                                from df
                                group by year
                                order by 1
                                limit 3
                                """)

    sui_per_year.show()

    // write
    sui_per_year.coalesce(1)
      .write.mode("overwrite")
      .option("header", "true")
      .option("sep", ",")
      .option("encoding", "euc-kr")
      .option("codec", "gzip")
      .csv("/data/sui_per_year")
  }
}