from tornado.ioloop import IOLoop
from tornado.web import RequestHandler, Application, url
import pycps

class UserSignUpHandler(RequestHandler):
	def post(self):
		user_id = self.get_argument('user_id')
		con = pycps.Connection('tcp://cloud-us-0.clusterpoint.com:9007', 'Handshake', 'vmmonkey@gmail.com', 'monkey', '100642')
		first_name = "Bob"
		last_name = "Smith"
		tag_line = "Test"
		picture_url = "www.picture.com"
		phone_number = '555-555-5555'
		email = 'yolo@swag.com'
		profile = {user_id:{
			'first_name': first_name, 
			'last_name': last_name, 
			'tag_line': tag_line, 
			'picture_url' :picture_url,
			'phone_number' : phone_number,
			'email' : email
			}
		}
		try:
			con.insert(profile)
		except pycps.APIError as e:
			print(e)
		return
		self.finish()