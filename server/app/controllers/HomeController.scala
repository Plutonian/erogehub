package controllers

import play.mvc.Results.ok
import play.mvc._


class HomeController extends Controller {
  def index: Result = ok("It works!")
}