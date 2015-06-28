from tornado.ioloop import IOLoop
from tornado.web import RequestHandler, Application, url
import pycps
import json

class ConnectionAdditionHandler(RequestHandler):
	def post(self):
		user_id = self.get_argument('user_id')	
		friend_id = self.get_argument('friend_id')	
		con = pycps.Connection('tcp://cloud-us-0.clusterpoint.com:9007', 'users', 'vmmonkey@gmail.com', 'monkey', '100642')
		try:
			response = con.lookup(user_id)
		except pycps.APIError as e:
			print 'no data available'
			return
		try:
			if response.get_documents:
				print("Found {0} documents: ".format(response.found))
				user = response.get_documents().items()[0][1]
				user['connections']+=','+friend_id
				con.update({user_id: user})
		except Exception as e:
			print(e)
			return

		print 'Added connection'
		self.finish()