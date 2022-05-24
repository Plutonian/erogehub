import {Pipe} from "@angular/core";

@Pipe({name:"imgurl"})
export  class ImgUrlPipl{

  transform(val:String){

    return `http://192.168.2.236/game/${val}.jpg`

  }

}
