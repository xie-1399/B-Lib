package lib.Math.Int
import spinal.core._
import spinal.lib._

//implement the Int Sqrt(10) calculation
//the method is : http://www.cppblog.com/QUIRE-0216/archive/2008/01/23/41714.html
// a pic show about this : https://blog.csdn.net/Chauncey_wu/article/details/102820782

//Todo about this
class IntSqrt(inputWidth:Int,outputWidth:Int) extends Component {
  val io = new Bundle{
      val option = slave Flow(UInt(inputWidth bits))
      val results = master Flow (UInt(outputWidth bits))
  }

  var r = (io.option.payload(io.option.payload.getWidth - 2 , 2 bits).resize(3).asSInt - 1)
  var q = Mux(r >= 0 , U"1",U"0")
  var k = io.option.payload.getWidth - 2
  var data = io.option.payload(0,io.option.payload.getWidth - 2 bits)
  var valid = io.option.valid


  while(q.getWidth < outputWidth){
    val insert_reg = false

    var valid_next = if (insert_reg) Reg(Bool()) else Bool()
    var r_next = if (insert_reg) Reg(SInt(r.getWidth + 2 bits)) else SInt(r.getWidth + 2 bits)
    var q_next = if (insert_reg) Reg(UInt(q.getWidth + 1 bits)) else UInt(q.getWidth + 1 bits)

    valid_next := io.option.valid
    r_next := ((r ## data(data.getWidth - 2, 2 bits) ## !r.msb).asSInt +
      (r.msb ? (U"0" @@ q @@ U"110") | (U"1" @@ ~q @@ U"101")).asSInt)(1, r_next.getWidth bits)

    when(!r_next.msb){
      q_next := q @@ U"1"
    }.otherwise{
      q_next := q @@ U"0"
    }

    valid := valid_next
    r := r_next
    q := q_next

    if (k > io.option.payload.getWidth / 2) {
      data = if (insert_reg) RegNext(data.resize(data.getWidth - 2)) else data.resize(data.getWidth - 2)
    }
    else {
      data = U(0, 2 bits)
    }
    k = k - 1
  }
  io.results.valid := valid
  io.results.payload := q

}


