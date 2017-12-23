@extends('admin.layout.base')

@section('title', 'Update Account Manager ')

@section('content')

<div class="content-area py-1">
    <div class="container-fluid">
    	<div class="box box-block bg-white">
    	    <a href="{{ route('admin.account-manager.index') }}" class="btn btn-default pull-right"><i class="fa fa-angle-left"></i> Back</a>

			<h5 style="margin-bottom: 2em;">Update Account Manager</h5>

            <form class="form-horizontal" action="{{route('admin.account-manager.update', $account->id )}}" method="POST" enctype="multipart/form-data" role="form">
            	{{csrf_field()}}
            	<input type="hidden" name="_method" value="PATCH">
				
				<div class="form-group row">
					<label for="name" class="col-xs-2 col-form-label">Full Name</label>
					<div class="col-xs-10">
						<input class="form-control" type="text" value="{{ $account->name }}" name="name" required id="name" placeholder="Full Name">
					</div>
				</div>

				<div class="form-group row">
					<label for="email" class="col-xs-2 col-form-label">Email</label>
					<div class="col-xs-10">
						<input class="form-control" type="text" value="{{ $account->email }}" readonly="true" name="email" required id="email" placeholder="Full Name">
					</div>
				</div>

				<div class="form-group row">
					<label for="mobile" class="col-xs-2 col-form-label">Mobile</label>
					<div class="col-xs-10">
						<input class="form-control" type="number" value="{{ $account->mobile }}" name="mobile" required id="mobile" placeholder="Mobile">
					</div>
				</div>

				<div class="form-group row">
					<label for="zipcode" class="col-xs-2 col-form-label"></label>
					<div class="col-xs-10">
						<button type="submit" class="btn btn-primary">Update Account Manager</button>
						<a href="{{route('admin.account-manager.index')}}" class="btn btn-default">Cancel</a>
					</div>
				</div>
			</form>
		</div>
    </div>
</div>

@endsection
